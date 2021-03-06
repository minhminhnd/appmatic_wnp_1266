package com.whitelabel.app.model;

/**
 * Created by Administrator on 2015/7/9.
 */
public class ShoppingCartDeleteCellEntity extends SVRReturnEntity{

    private int summaryQty;
    private int status;
    private String subTotal;
    private String grandTotal;
    private ShoppingDiscountBean shipping;
    private ShoppingDiscountBean discount;
    private String inStock;
    private int canUseCampaign;//1 can use campaign, otherwise no.
    private String popupText;
    private String androidCampBanner;
    private boolean isCampaignProduct;
    private KeyValueBean params;
    private Object param;
    private ShoppingCarStoreCreditBean storeCreditMessage;
    private ShoppingCarStoreCreditBean storeCredit;

    public ShoppingCarStoreCreditBean getStoreCredit() {
        return storeCredit;
    }

    public void setStoreCredit(ShoppingCarStoreCreditBean storeCredit) {
        this.storeCredit = storeCredit;
    }

    public ShoppingCarStoreCreditBean getStoreCreditMessage() {
        return storeCreditMessage;
    }

    public void setStoreCreditMessage(ShoppingCarStoreCreditBean storeCreditMessage) {
        this.storeCreditMessage = storeCreditMessage;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public KeyValueBean getParams() {
        return params;
    }

    public void setParams(KeyValueBean params) {
        this.params = params;
    }

    public boolean isCampaignProduct() {
        return isCampaignProduct;
    }

    public void setIsCampaignProduct(boolean isCampaignProduct) {
        this.isCampaignProduct = isCampaignProduct;
    }

    public String getAndroidCampBanner() {
        return androidCampBanner;
    }

    public void setAndroidCampBanner(String androidCampBanner) {
        this.androidCampBanner = androidCampBanner;
    }

    public String getPopupText() {
        return popupText;
    }

    public void setPopupText(String popupText) {
        this.popupText = popupText;
    }

    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    public ShoppingDiscountBean getDiscount() {
        return discount;
    }

    public void setDiscount(ShoppingDiscountBean discount) {
        this.discount = discount;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public ShoppingDiscountBean getShipping() {
        return shipping;
    }

    public void setShipping(ShoppingDiscountBean shipping) {
        this.shipping = shipping;
    }

    public int getSummaryQty() {
        return summaryQty;
    }

    public void setSummaryQty(int summaryQty) {
        this.summaryQty = summaryQty;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCanUseCampaign() {
        return canUseCampaign;
    }

    public void setCanUseCampaign(int canUseCampaign) {
        this.canUseCampaign = canUseCampaign;
    }
}
