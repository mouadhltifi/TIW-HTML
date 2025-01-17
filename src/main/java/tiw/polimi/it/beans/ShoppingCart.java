package tiw.polimi.it.beans;

import tiw.polimi.it.stuffToDelete.MyColors;

import java.util.List;

public class ShoppingCart {
    private final List<LightItem> item;
    private double totalPrice;
    private int quantity;
    private final Seller seller;

    public ShoppingCart(List<LightItem> item, double totalPrice, int quantity, Seller seller) {
        this.item = item;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.seller = seller;
    }


    public List<LightItem> getItem() {
        return item;
    }

    public double getItemsPrice() {
        return totalPrice;
    }
    public double getTotalPrice() throws Exception {
        return totalPrice + this.getShippingPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public Seller getSeller() {
        return seller;
    }

    public double getShippingPrice() throws Exception {
        if (totalPrice >= seller.getFreeShipment()) {
            return 0.0;
        }
        else {
            for (ShippingPolicy s : seller.getsPolicy()
            ) {
                if (this.quantity >= s.getMinItems() && this.quantity <= s.getMaxItems()) {
                    return s.getPrice();
                }

            }
            System.out.println("fouri da range di politica di spedizione ");
            return 5;
        }
       // throw new Exception("problema con calcolo fascia di spedzione");


    }

    public boolean checkSellerById(int sellerId ){
        return sellerId == this.seller.getSellerId();
    }

    public void updateSellerCart(Seller seller, int quantity, Item itemAdded, Double price) {

        this.quantity += quantity;
        this.totalPrice+=quantity*price;


        for (LightItem i: this.item
             ) {
            //increase quantity
            if(i.getItemId()==itemAdded.getId_item()){
                i.incrementQuantity(quantity);
                return;
            }
        }

        this.item.add(
                new LightItem(
                        itemAdded.getName(),
                        itemAdded.getId_item(),
                        quantity
                )
        );
    }

    @Override
    public String toString() {
        return MyColors.ANSI_GREEN +  "stampa ShoppingCart{" +
                "item=" + item +
                ", totalPrice=" + totalPrice +
                ", quantity=" + quantity +
                ", seller=" + seller +
                '}' + MyColors.ANSI_RESET;
    }



    /*
    item1  vend1 ===
    item2 vend2
    item3 vend1 ===

    [2,4,6,8]
    insert(3,6)
     */


    /**
     * private List<Item> itemList;
     *     private List<Seller> sellerList;
     *
     *     //ridondante items
     *
     *     struct miaStruct{
     *         fornitore;
     *         List Map (id_inVendita,prezzo)  --->
     *         List Map (id_articolo,articolo)
     *         prezzoTotale=+- qualcosa;
     *         costoDispedizione;
     *     }
     *
     *     map.insert(id_articolo,prezzo);
     *
     *     session.setAttribut("id_venditore",miaStruct)
     */


}





