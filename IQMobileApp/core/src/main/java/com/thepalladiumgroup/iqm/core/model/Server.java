package com.thepalladiumgroup.iqm.core.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Koske Kimutai [2016/04/21]
 */
public class Server extends Entity implements Comparable<Server> {

    @DatabaseField
    private String name;
    @DatabaseField
    private String url;
    @DatabaseField
    private int order;
    @DatabaseField
    private boolean isEnabled;

    public Server() {
    }

    public Server(boolean isEnabled, String name, int order, String url) {
        this.isEnabled = isEnabled;
        this.name = name;
        this.order = order;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlLink() {
        String link = "<a href=" + url + ">" + url + "</a>";
        return link;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public String toString() {
        return name + " [" + url + "]";
    }

    @Override
    public int compareTo(Server server) {
        return this.order > server.order ? 1 : (this.order < server.order ? -1 : 0);
    }
}
