package org.komine.wca.api.rest.util;

import java.util.Date;

public class WCADocument {
	String id;
	String collection;
	Date updated;
	String title;
	String summary;
	String language;
	String documentSource;
	String link_via;
	
	public WCADocument() {
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLink_via() {
		return link_via;
	}

	public void setLink_via(String link_via) {
		this.link_via = link_via;
	}

	public String getDocumentSource() {
		return documentSource;
	}

	public void setDocumentSource(String documentSource) {
		this.documentSource = documentSource;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer("WCADocument: ");
		buf.append("id=").append(id).append(", ");
		buf.append("collection=").append(collection).append(", ");
		buf.append("updated=").append(updated.toString()).append(", ");
		buf.append("title=").append(title).append(", ");
		buf.append("summary=").append(summary).append(", ");
		buf.append("language=").append(language).append(", ");
		buf.append("documentSource=").append(documentSource).append(", ");
		buf.append("link_via=").append(link_via);
		return buf.toString();
	}
}
