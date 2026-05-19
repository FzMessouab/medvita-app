import { useEffect, useState } from 'react';
import { RiShoppingCart2Line } from 'react-icons/ri';
import { toast } from 'sonner';
import { CartItem } from './MedicalCart';
import { ProductType } from '../products/Product';

export default function AddToQuote({ product }: { product: ProductType }) {
    const [quantity, setQuantity] = useState(1);

    const increment = () => setQuantity((prev) => prev + 1);
    const decrement = () => setQuantity((prev) => Math.max(1, prev - 1));

    function addToCart() {
        const itemFromCart = getItemFromCart();

        if (itemFromCart.item === null) {
            itemFromCart.cartItems.push({ product, quantity, mode: 'buy', rentStart: new Date(), rentEnd: new Date() });
        }
        else {
            if (quantity > 0) {
                itemFromCart.cartItems[itemFromCart.index].quantity = quantity;
            }
            else {
                itemFromCart.cartItems = itemFromCart.cartItems.filter((item: any, index: number) => index !== itemFromCart.index);
            }
        }

        localStorage.setItem('cart', JSON.stringify(itemFromCart.cartItems));

        toast.success(`Produit "${decodeURIComponent(product.name)}" ajoute au devis avec une quantite de ${quantity}.`);
    }

    function getItemFromCart(): { cartItems: CartItem[], item: CartItem | null, index: number } {
        const cart = localStorage.getItem('cart');

        const cartItems: CartItem[] = cart ? JSON.parse(cart) : [];

        const existingItemIndex = cartItems.findIndex((item: any) => item.product.name == product.name);

        if (existingItemIndex !== -1) {
            return { cartItems, item: cartItems[existingItemIndex], index: existingItemIndex };
        }

        return { cartItems, item: null, index: -1 };
    }

    useEffect(() => {
        const itemFromCart = getItemFromCart();

        if (itemFromCart.item) {
            setQuantity(itemFromCart.item.quantity);
        }
    }, []);

    return (
        <div className="flex items-center gap-4">
            <div className="flex items-center bg-red-50 rounded-full px-3 py-2">
                <button
                    onClick={decrement}
                    className="bg-white w-8 h-8 flex items-center justify-center rounded-full text-gray-700 font-medium text-xl"
                >
                    –
                </button>
                <span className="mx-3 text-gray-800 font-semibold text-lg">{quantity}</span>
                <button
                    onClick={increment}
                    className="bg-white w-8 h-8 flex items-center justify-center rounded-full text-gray-700 font-medium text-xl"
                >
                    +
                </button>
            </div>

            <button onClick={addToCart} className="bg-orange-500 hover:bg-orange-600 transition text-white font-medium rounded-full flex items-center px-6 py-3 text-base">
                <RiShoppingCart2Line className="w-5 h-5 mr-2" />
                Ajouter au devis
            </button>
        </div>
    );
}
