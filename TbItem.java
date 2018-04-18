package com.tidfore.demo.bean;

import com.tidfore.demo.solr.SolrField;

import org.apache.solr.common.SolrInputDocument;

import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.*;
import java.io.Serializable;
@SolrDocument(solrCoreName = "new_core")
@Entity
@Table(name="tb_item")
 public class TbItem implements Serializable {

    @SolrField("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @SolrField(value = "item_title",highlight = true)
    private String title;
    @SolrField("item_desc")
    private String desc;
    @SolrField("item_url")
    private String url;
    @SolrField("item_type")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public SolrInputDocument toSolr() {
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", id+"");//必须的域 的名称是id 它的类型：String
        document.addField("item_desc", desc);
        document.addField("item_url",url);
        return  document;
    }
}