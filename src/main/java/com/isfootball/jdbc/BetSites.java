package com.isfootball.jdbc;

import org.springframework.data.domain.Persistable;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class is generated by Spring Data Jdbc code generator.
 *
 * @author Spring Data Jdbc Code Generator
 */
public class BetSites implements Persistable<String>
{

	private static final long serialVersionUID = 1L;

	private String site;

	private String url;

	private String comments;

	private transient boolean persisted;


	public BetSites ()
	{

	}

	public String getId ()
	{
		return this.site;
	}

	public boolean isNew ()
	{
		return this.site == null;
	}

	public void setSite (String site)
	{
		this.site = site;
	}

	public String getSite ()
	{
		return this.site;
	}

	public void setUrl (String url)
	{
		this.url = url;
	}

	public String getUrl ()
	{
		return this.url;
	}

	public void setComments (String comments)
	{
		this.comments = comments;
	}

	public String getComments ()
	{
		return this.comments;
	}

	public void setPersisted (Boolean persisted)
	{
		this.persisted = persisted;
	}

	public Boolean getPersisted ()
	{
		return this.persisted;
	}

	@Override
	public String toString () 
	{
		return ToStringBuilder.reflectionToString (this); 
	}

	/* START Do not remove/edit this line. CodeGenerator will preserve any code between start and end tags.*/

	/* END Do not remove/edit this line. CodeGenerator will preserve any code between start and end tags.*/

}