package com.medvita.backend.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.medvita.backend.dto.CartCheckoutRequestDTO;
import com.medvita.backend.dto.InvoiceResponseDTO;
import com.medvita.backend.dto.PurchaseRequestDTO;
import com.medvita.backend.dto.PurchaseResponseDTO;
import com.medvita.backend.dto.RentalRequestDTO;
import com.medvita.backend.dto.RentalResponseDTO;
import com.medvita.backend.entities.Client;
import com.medvita.backend.entities.Invoice;
import com.medvita.backend.entities.Purchase;
import com.medvita.backend.entities.Rental;
import com.medvita.backend.mappers.InvoiceMapper;
import com.medvita.backend.mappers.RentalMapper;
import com.medvita.backend.services.ClientService;
import com.medvita.backend.services.EquipmentService;
import com.medvita.backend.services.InvoiceService;
import com.medvita.backend.services.PurchaseService;
import com.medvita.backend.services.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final PurchaseService purchaseService;
    private final RentalService rentalService;
    private final RentalMapper rentalMapper;
    private final EquipmentService equipmentService;
    private final ClientService clientService;
    private final InvoiceService invoiceService;
    private final InvoiceMapper invoiceMapper;

    public CartController(PurchaseService purchaseService,
                          RentalService rentalService,
                          RentalMapper rentalMapper,
                          EquipmentService equipmentService,
                          ClientService clientService,
                          InvoiceService invoiceService,
                          InvoiceMapper invoiceMapper) {
        this.purchaseService = purchaseService;
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
        this.equipmentService = equipmentService;
        this.clientService = clientService;
        this.invoiceService = invoiceService;
        this.invoiceMapper = invoiceMapper;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody JsonNode payload) {
        Long clientId = resolveOrCreateClientId(payload);
        String paymentMethod = resolvePaymentMethod(payload);

        validatePurchaseItemsStock(payload);
        List<PurchaseResponseDTO> purchases = processPurchaseItems(payload, clientId, paymentMethod);
        List<RentalResponseDTO> rentals = processRentalItems(payload, clientId, paymentMethod);

        if (purchases.isEmpty() && rentals.isEmpty()) {
            CartCheckoutRequestDTO request = toCheckoutRequest(payload, clientId, paymentMethod);
            if (isRental(request)) {
                Rental createdRental = rentalService.createRental(toRentalRequest(request));
                rentals.add(rentalMapper.toDto(createdRental));
            } else {
                purchases.add(purchaseService.processPurchase(toPurchaseRequest(request)));
            }
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("clientId", clientId);
        response.put("purchaseCount", purchases.size());
        response.put("rentalCount", rentals.size());
        response.put("purchases", purchases);
        response.put("rentals", rentals);
        if (!purchases.isEmpty() || !rentals.isEmpty()) {
            InvoiceResponseDTO invoice = createCartInvoiceResponse(clientId, purchases, rentals);
            response.put("invoice", invoice);
            response.put("invoiceNumber", invoice.getInvoiceNumber());
        }
        return ResponseEntity.ok(response);
    }

    private InvoiceResponseDTO createCartInvoiceResponse(Long clientId,
                                                         List<PurchaseResponseDTO> purchases,
                                                         List<RentalResponseDTO> rentals) {
        Client client = clientService.getById(clientId);
        List<Purchase> purchaseEntities = purchases.stream()
                .map(purchase -> purchaseService.getById(purchase.getId()))
                .toList();
        List<Rental> rentalEntities = rentals.stream()
                .map(rental -> rentalService.getById(rental.getId()))
                .toList();
        Invoice invoice = invoiceService.generateCartInvoice(client, purchaseEntities, rentalEntities);
        return invoiceMapper.toDto(invoice);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", exception.getStatusCode().value());
        body.put("error", exception.getStatusCode().toString());
        body.put("message", exception.getReason());
        return ResponseEntity.status(exception.getStatusCode()).body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.toString());
        body.put("message", exception.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    private boolean isRental(CartCheckoutRequestDTO request) {
        if (request.getStartDate() != null || request.getEndDate() != null) {
            return true;
        }

        return matchesType(request.getCheckoutType(), "rental")
                || matchesType(request.getType(), "rental")
                || matchesType(request.getCheckoutType(), "location")
                || matchesType(request.getType(), "location");
    }

    private boolean matchesType(String actual, String expected) {
        return actual != null && actual.trim().equalsIgnoreCase(expected);
    }

    private CartCheckoutRequestDTO toCheckoutRequest(JsonNode payload, Long clientId, String paymentMethod) {
        CartCheckoutRequestDTO request = new CartCheckoutRequestDTO();
        request.setClientId(clientId);
        request.setEquipmentId(resolveEquipmentId(payload));
        request.setQuantity(readInteger(payload, "quantity", "qty", "count"));
        request.setPaymentMethod(paymentMethod);
        request.setCheckoutType(readText(payload, "checkoutType", "checkout_type"));
        request.setType(readText(payload, "type", "orderType", "mode"));
        request.setStartDate(readDate(payload, "startDate", "start_date", "rentalStartDate"));
        request.setEndDate(readDate(payload, "endDate", "end_date", "rentalEndDate"));
        validateCheckoutRequest(request);
        return request;
    }

    private Long resolveOrCreateClientId(JsonNode payload) {
        Long clientId = readLong(payload, "clientId", "client_id", "userId", "user_id");
        if (clientId != null) {
            return clientId;
        }

        clientId = readNestedLong(payload, List.of("client", "user", "customer"), List.of("id", "clientId", "userId"));
        if (clientId != null) {
            return clientId;
        }

        String email = readText(payload, "clientEmail", "email", "userEmail");
        if (email == null) {
            email = readNestedText(payload, List.of("client", "user", "customer"), List.of("email"));
        }
        if (email != null) {
            try {
                return clientService.getClientByEmail(email).getId();
            } catch (RuntimeException ignored) {
                return createClientFromPayload(payload, email).getId();
            }
        }

        List<com.medvita.backend.entities.Client> activeClients = clientService.getActifClients();
        if (activeClients.size() == 1) {
            return activeClients.get(0).getId();
        }

        return null;
    }

    private Client createClientFromPayload(JsonNode payload, String email) {
        JsonNode clientNode = firstObject(payload, "client", "user", "customer");
        if (clientNode == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing client information in checkout payload");
        }

        Client client = Client.builder()
                .fullName(requiredText(clientNode, "fullName", "name"))
                .email(email)
                .telephone(defaultText(readText(clientNode, "phone", "telephone"), "+0000000000"))
                .adresse(defaultText(readText(clientNode, "address", "adresse", "streetAddress"), "Not provided"))
                .ville(defaultText(readText(clientNode, "city", "ville"), "Unknown"))
                .codePostal(defaultText(readText(clientNode, "postalCode", "zipCode", "codePostal"), "00000"))
                .pays(defaultText(readText(clientNode, "country", "pays"), "Unknown"))
                .numeroSecu(readText(clientNode, "socialSecurityNumber", "numeroSecu", "socialSecurity"))
                .build();
        return clientService.create(client);
    }

    private Long resolveEquipmentId(JsonNode payload) {
        Long equipmentId = readLong(payload, "equipmentId", "equipment_id", "productId", "product_id", "itemId", "item_id", "id");
        if (equipmentId != null) {
            return equipmentId;
        }

        equipmentId = readNestedLong(payload, List.of("equipment", "product", "item"), List.of("id", "equipmentId", "productId", "itemId"));
        if (equipmentId != null) {
            return equipmentId;
        }

        String equipmentName = readText(payload, "equipmentName", "name", "productName", "title");
        if (equipmentName == null) {
            equipmentName = readNestedText(payload, List.of("equipment", "product", "item"), List.of("name", "title", "equipmentName"));
        }
        if (equipmentName != null) {
            return equipmentService.getByName(equipmentName).getId();
        }

        return null;
    }

    private void validateCheckoutRequest(CartCheckoutRequestDTO request) {
        if (request.getClientId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing clientId in checkout payload");
        }
        if (request.getEquipmentId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing equipmentId in checkout payload");
        }
        if (request.getPaymentMethod() == null || request.getPaymentMethod().isBlank()) {
            request.setPaymentMethod("CARTE");
        }
        if (request.getQuantity() == null || request.getQuantity() < 1) {
            request.setQuantity(1);
        }
        if (isRental(request) && (request.getStartDate() == null || request.getEndDate() == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rental checkout requires startDate and endDate");
        }
    }

    private List<PurchaseResponseDTO> processPurchaseItems(JsonNode payload, Long clientId, String paymentMethod) {
        JsonNode itemsNode = payload.get("purchaseItems");
        if (itemsNode == null || !itemsNode.isArray()) {
            return List.of();
        }

        List<PurchaseResponseDTO> responses = new ArrayList<>();
        for (JsonNode itemNode : itemsNode) {
            PurchaseRequestDTO request = new PurchaseRequestDTO();
            request.setClientId(clientId);
            request.setEquipmentId(resolveEquipmentId(itemNode));
            request.setQuantity(defaultQuantity(readInteger(itemNode, "quantity", "qty", "count")));
            request.setPaymentMethod(paymentMethod);
            validatePurchaseItem(request);
            responses.add(purchaseService.processPurchase(request));
        }
        return responses;
    }

    private void validatePurchaseItemsStock(JsonNode payload) {
        JsonNode itemsNode = payload.get("purchaseItems");
        if (itemsNode == null || !itemsNode.isArray()) {
            return;
        }

        Map<Long, Integer> requestedQuantities = new HashMap<>();
        for (JsonNode itemNode : itemsNode) {
            Long equipmentId = resolveEquipmentId(itemNode);
            Integer quantity = defaultQuantity(readInteger(itemNode, "quantity", "qty", "count"));
            if (equipmentId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing equipmentId in purchase item");
            }
            requestedQuantities.merge(equipmentId, quantity, Integer::sum);
        }

        for (Map.Entry<Long, Integer> entry : requestedQuantities.entrySet()) {
            equipmentService.checkEquipmentAvailability(entry.getKey(), entry.getValue());
        }
    }

    private List<RentalResponseDTO> processRentalItems(JsonNode payload, Long clientId, String paymentMethod) {
        JsonNode itemsNode = payload.get("rentalItems");
        if (itemsNode == null || !itemsNode.isArray()) {
            return List.of();
        }

        List<RentalResponseDTO> responses = new ArrayList<>();
        for (JsonNode itemNode : itemsNode) {
            CartCheckoutRequestDTO request = new CartCheckoutRequestDTO();
            request.setClientId(clientId);
            request.setEquipmentId(resolveEquipmentId(itemNode));
            request.setQuantity(defaultQuantity(readInteger(itemNode, "quantity", "qty", "count")));
            request.setPaymentMethod(paymentMethod);
            request.setStartDate(readDate(itemNode, "startDate", "start_date", "rentalStartDate"));
            request.setEndDate(readDate(itemNode, "endDate", "end_date", "rentalEndDate"));
            request.setType("rental");
            validateCheckoutRequest(request);
            responses.add(rentalMapper.toDto(rentalService.createRental(toRentalRequest(request))));
        }
        return responses;
    }

    private void validatePurchaseItem(PurchaseRequestDTO request) {
        if (request.getEquipmentId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing equipmentId in purchase item");
        }
    }

    private String resolvePaymentMethod(JsonNode payload) {
        String paymentMethod = readText(payload, "paymentMethod", "payment_method", "payment");
        if (paymentMethod == null) {
            paymentMethod = readNestedText(payload, List.of("payment", "checkout"), List.of("method", "paymentMethod"));
        }
        if (paymentMethod == null) {
            return null;
        }

        String normalized = paymentMethod.trim().toUpperCase()
                .replace("È", "E")
                .replace("É", "E")
                .replace("Ê", "E");

        if ("CARD".equals(normalized) || "CB".equals(normalized)) {
            return "CARTE";
        }
        if ("CASH".equals(normalized) || "ESPECES".equals(normalized) || "ESPÈCES".equals(paymentMethod.trim().toUpperCase())) {
            return "ESPECES";
        }
        if ("BANK_TRANSFER".equals(normalized) || "TRANSFER".equals(normalized) || "WIRE".equals(normalized)) {
            return "VIREMENT";
        }

        return normalized;
    }

    private int defaultQuantity(Integer quantity) {
        return quantity == null || quantity < 1 ? 1 : quantity;
    }

    private Long readLong(JsonNode node, String... fieldNames) {
        for (String fieldName : fieldNames) {
            JsonNode child = node.get(fieldName);
            if (child == null || child.isNull()) {
                continue;
            }
            if (child.isNumber()) {
                return child.longValue();
            }
            if (child.isTextual()) {
                try {
                    return Long.parseLong(child.asText().trim());
                } catch (NumberFormatException ignored) {
                    return null;
                }
            }
        }
        return null;
    }

    private Integer readInteger(JsonNode node, String... fieldNames) {
        for (String fieldName : fieldNames) {
            JsonNode child = node.get(fieldName);
            if (child == null || child.isNull()) {
                continue;
            }
            if (child.isNumber()) {
                return child.intValue();
            }
            if (child.isTextual()) {
                try {
                    return Integer.parseInt(child.asText().trim());
                } catch (NumberFormatException ignored) {
                    return null;
                }
            }
        }
        return null;
    }

    private String readText(JsonNode node, String... fieldNames) {
        for (String fieldName : fieldNames) {
            JsonNode child = node.get(fieldName);
            if (child != null && !child.isNull() && child.isValueNode()) {
                String value = child.asText();
                if (!value.isBlank()) {
                    return value;
                }
            }
        }
        return null;
    }

    private LocalDate readDate(JsonNode node, String... fieldNames) {
        String value = readText(node, fieldNames);
        if (value == null) {
            return null;
        }
        return LocalDate.parse(value);
    }

    private Long readNestedLong(JsonNode node, List<String> parentFields, List<String> childFields) {
        for (String parentField : parentFields) {
            JsonNode parent = node.get(parentField);
            if (parent == null || parent.isNull() || !parent.isObject()) {
                continue;
            }
            Long value = readLong(parent, childFields.toArray(String[]::new));
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private String readNestedText(JsonNode node, List<String> parentFields, List<String> childFields) {
        for (String parentField : parentFields) {
            JsonNode parent = node.get(parentField);
            if (parent == null || parent.isNull() || !parent.isObject()) {
                continue;
            }
            String value = readText(parent, childFields.toArray(String[]::new));
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private JsonNode firstObject(JsonNode node, String... fieldNames) {
        for (String fieldName : fieldNames) {
            JsonNode child = node.get(fieldName);
            if (child != null && child.isObject()) {
                return child;
            }
        }
        return null;
    }

    private String requiredText(JsonNode node, String... fieldNames) {
        String value = readText(node, fieldNames);
        if (value == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Missing required client field: " + fieldNames[0]
            );
        }
        return value;
    }

    private String defaultText(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private PurchaseRequestDTO toPurchaseRequest(CartCheckoutRequestDTO request) {
        PurchaseRequestDTO purchaseRequest = new PurchaseRequestDTO();
        purchaseRequest.setClientId(request.getClientId());
        purchaseRequest.setEquipmentId(request.getEquipmentId());
        purchaseRequest.setQuantity(request.getQuantity() == null ? 1 : request.getQuantity());
        purchaseRequest.setPaymentMethod(request.getPaymentMethod());
        purchaseRequest.setVersion(request.getVersion());
        return purchaseRequest;
    }

    private RentalRequestDTO toRentalRequest(CartCheckoutRequestDTO request) {
        RentalRequestDTO rentalRequest = new RentalRequestDTO();
        rentalRequest.setClientId(request.getClientId());
        rentalRequest.setEquipmentId(request.getEquipmentId());
        rentalRequest.setQuantity(request.getQuantity() == null ? 1 : request.getQuantity());
        rentalRequest.setPaymentMethod(request.getPaymentMethod());
        rentalRequest.setStartDate(request.getStartDate());
        rentalRequest.setEndDate(request.getEndDate());
        rentalRequest.setVersion(request.getVersion());
        return rentalRequest;
    }
}
