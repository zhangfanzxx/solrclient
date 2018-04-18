package com.tidfore.demo;

import com.tidfore.demo.bean.TbItem;
import com.tidfore.demo.solr.SolrBean;
import com.tidfore.demo.solr.SolrUtil;
import org.apache.catalina.LifecycleState;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SolrTest {

    private SolrUtil solrUtil;

    @Before
    public void setSolr(){
        solrUtil =new SolrUtil();
        SolrServer solrServer=new HttpSolrServer("http://localhost:8080/solr/new_core");
        solrUtil.setSolrServer(solrServer);
    }

    @Test
    public void save(){
        TbItem item=new TbItem();
        item.setUrl("www.baidu.com");
        item.setType("前端");
        item.setDesc("测试保存");
        item.setId("1532");
        item.setTitle("测试");
        try {
            solrUtil.saveBean(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void saves(){
        List<TbItem> list=new ArrayList<>();
        TbItem item=new TbItem();
        item.setUrl("www.baidu.com");
        item.setType("前端1");
        item.setDesc("测试保存1");
        item.setId("15321");
        item.setTitle("测试1");
        list.add(item);
         item=new TbItem();
        item.setUrl("www.baidu.com");
        item.setType("前端2");
        item.setDesc("测试保存2");
        item.setId("15322");
        item.setTitle("测试2");
        list.add(item);
        try {
            solrUtil.saveBeans(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void query(){
        SolrQuery query=new SolrQuery();
        query.setQuery("测试");
        query.setStart(0);//page-1 * rows
        query.setRows(6);//rows
        //3.6设置默认的搜索域
        query.set("df", "item_keywords");
        //3.7 开启高亮 设置高亮显示的域  设置高亮的前缀 和后缀
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");
        //4.执行查询
        try {
            SolrBean solrBean = solrUtil.query(query, TbItem.class);
            System.out.println(solrBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(1);
    }
}
