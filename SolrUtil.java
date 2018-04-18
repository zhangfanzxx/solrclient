package com.tidfore.demo.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class SolrUtil {
    @Autowired
    SolrServer solrServer;

    public void setSolrServer(SolrServer solrServer) {
        this.solrServer = solrServer;
    }

    public  void  saveBean(Object object) throws Exception {
        solrServer.add(getDocument(object));
        solrServer.commit();
    }

    public <T>void saveBeans(List<T> objects) throws IOException, SolrServerException, IllegalAccessException {
        List<SolrInputDocument> list=new ArrayList<>();
        for (T object : objects) {
            list.add(getDocument(object));
        }
        solrServer.add(list);
        solrServer.commit();
    }

    public <T>SolrBean query(SolrQuery query, Class<T> clazz) throws SolrServerException, IllegalAccessException, InstantiationException {
        List<T> list =new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        HashMap<Field,SolrField> map =new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            SolrField annotation = field.getAnnotation(SolrField.class);
            if(annotation!=null) map.put(field,annotation);
        }
        if(query.getRows()==null||query.getRows()==0) query.setRows(10);
        if( query.getStart()==null)query.setStart(0);
        QueryResponse response = solrServer.query(query);
        SolrDocumentList results = response.getResults();
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        for (SolrDocument solrDocument : results) {
            T newInstance = clazz.newInstance();
            for (Map.Entry<Field, SolrField> entry : map.entrySet()) {
                Field key = entry.getKey();
                key.setAccessible(true);
                SolrField value = entry.getValue();
                String string="";
                if(value.highlight()){
                    List<String> hlist = highlighting.get(solrDocument.get("id")).get(value.value());
                    if(hlist!=null && hlist.size()>0){
                        string = hlist.get(0);
                    }else{
                        string = solrDocument.get(value.value())+"";
                    }
                }else{
                    string = solrDocument.get(value.value())+"";
                }
                key.set(newInstance, string);
            }
            list.add(newInstance);
        }
        SolrBean<T> solrBean=new SolrBean<>();
        solrBean.setList(list);
        solrBean.setTotalSize(results.getNumFound());
        solrBean.setSize(results.size());
        solrBean.setPageSize(query.getStart()/query.getRows()+1);
        solrBean.setTotalPage(results.size()/query.getRows());
        return solrBean;
    }

    private <T>SolrInputDocument getDocument(T object) throws IllegalAccessException {
        SolrInputDocument document = new SolrInputDocument();
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            SolrField annotation = field.getAnnotation(SolrField.class);
            if(annotation!=null){
                document.addField(annotation.value(),field.get(object));
            }
        }
        return document;
    }

}

