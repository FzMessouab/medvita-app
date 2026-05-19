import Categories from "@/components/categories/Categories";
import Banner from "@/components/Banner";
import Products from "@/components/products/Products";

export default function Home() {
  return (
    <div>
      <Banner />
      <Categories />
      <Products />
    </div>
  );
}