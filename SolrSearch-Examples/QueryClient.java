package com.example.query;

import java.net.MalformedURLException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.FacetParams;

public class QueryClient {
	public static void main(String[] args) throws MalformedURLException, SolrServerException {
	     SolrServer solr = new HttpSolrServer("http://search01.dev.fbcloud.net:9001/solr/fb_buys_fed_shard1_replica1");

	     SolrQuery query = new SolrQuery();
	     query.setQuery("(BUY_ID:732037 BUY_ID:732076 BUY_ID:732075 BUY_ID:732054 BUY_ID:732014 BUY_ID:731828 BUY_ID:732072 BUY_ID:731777 BUY_ID:732043 BUY_ID:732039 BUY_ID:732052 BUY_ID:732032 BUY_ID:732041 BUY_ID:732074 BUY_ID:731976) AND FB_KW_SELLER:'test'");//AND (*:* NOT SHIPPING_COUNTRY:*)
	     //query.addFilterQuery("cat:electronics","store:amazon.com");
	     query.setFields("BUY_ID","BUY_NUMBER","BUY_DESCRIPTION","ORG_DISPLAY_NAME", "CONTRACT_TYPE", "BUY_ENDDATE", "MARKET_SECTOR_ID", "OPEN_DATE", "DECREMENT_TYPE_ID", "SELECTED_BID_ID", "CURRENT_BUY_STATUS", "IS_SC");
	     query.setStart(0);    
	     //query.set("defType", "edismax");

	     
	     query.addFacetField("CONTRACT_TYPE_NAME");
	     query.addFacetField("SEARCH_CATEGORY_SEARCH_CATEGORY");
	     query.addFacetField("SETASIDE");
	     
	     query.addFacetField("SHIPPING_STATE");
	     String facetSort = String.format("f.%s.facet.sort", "SHIPPING_STATE");
	     query.set(facetSort, FacetParams.FACET_SORT_INDEX);
	     String missingQuery = String.format("f.%s.facet.missing", "SHIPPING_STATE");
	     query.set(missingQuery, true);
	     
	     
	     
	     query.addFacetField("SHIPPING_COUNTRY");	
	     String facetSort1 = String.format("f.%s.facet.sort", "SHIPPING_COUNTRY");
	     query.set(facetSort1, FacetParams.FACET_SORT_INDEX);
	     String missingQuery1 = String.format("f.%s.facet.missing", "SHIPPING_COUNTRY");
	     query.set(missingQuery1, true);     
	     
	     
	     
	     
	     QueryResponse response = solr.query(query);
	     
	     
	     //query.setFacet(true);
	     //query.setFacetMinCount(0);
	     
	     query.remove( FacetParams.FACET_FIELD );
	     query.addFacetField( "features" );
	     query.setFacetMinCount( 0 );
	     query.setFacet( true );
	     query.setRows( 0 );
	       
	     
	     SolrDocumentList results = response.getResults();
	     for (int i = 0; i < results.size(); ++i) {
	       System.out.println(results.get(i));
	     }
	     
	     System.out.println(" ========================== facet ================================ ");
	     for(FacetField field : response.getFacetFields()){
	    	 System.out.println(field);
	     }
	     
	     
	   }

}
