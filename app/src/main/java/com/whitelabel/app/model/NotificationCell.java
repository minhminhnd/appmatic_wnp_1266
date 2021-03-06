package com.whitelabel.app.model;



import java.io.Serializable;

/**
 * 购物车实体类
 */
public class NotificationCell implements Serializable{
    private String id;
    private String  created_at;
    private int unread;
//    private String sent_at;
    private String notification_id;
    private int state;
    private String title;
    private String body;
    private String attached_link;
    private int internal_type;
    private int attached_link_type; //1External Link   2为Internal Link
    private NotificationCategory category;
    private String productId;
    private String landingPageId;
    private String active;
    private String code;
    private String banner;
    private String expiryTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }
    public String getBanner() {
        return banner;
    }
    public void setBanner(String banner) {
        this.banner = banner;
    }
    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getLandingPageId() {
        return landingPageId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setLandingPageId(String landingPageId) {
        this.landingPageId = landingPageId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getInternal_type() {
        return internal_type;
    }

    public void setInternal_type(int internal_type) {
        this.internal_type = internal_type;
    }

    public NotificationCategory getCategory() {
        return category;
    }

    public void setCategory(NotificationCategory category) {
        this.category = category;
    }

    public int getAttached_link_type() {
        return attached_link_type;
    }

    public void setAttached_link_type(int attached_link_type) {
        this.attached_link_type = attached_link_type;
    }
    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttached_link() {
        return attached_link;
    }

    public void setAttached_link(String attached_link) {
        this.attached_link = attached_link;
    }
}
