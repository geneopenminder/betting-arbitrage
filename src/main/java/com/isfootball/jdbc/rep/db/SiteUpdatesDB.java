package com.isfootball.jdbc.rep.db;

import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.isfootball.jdbc.SiteUpdates;
import java.sql.Timestamp;

/**
 * This class is generated by Spring Data Jdbc code generator.
 *
 * @author Spring Data Jdbc Code Generator
 */
public class SiteUpdatesDB
{

	private static String TABLE_NAME = "site_updates";

	private static String TABLE_ALIAS = "SITE_UPDATES";

	public static String getTableName()
	{
		return TABLE_NAME;
	}

	public static String getTableAlias()
	{
		return TABLE_NAME + " as " + TABLE_ALIAS;
	}

	public static String getAlias()
	{
		return TABLE_ALIAS;
	}

	public static String selectAllColumns(boolean ... useAlias)
	{
		return (useAlias[0] ? TABLE_ALIAS : TABLE_NAME) + ".*";
	}

	public enum COLUMNS
	{
		ID("id"),
		SITE("site"),
		DATE_ADDED("date_added"),
		EVENTS_NUMBER("events_number"),
		UPDATE_TIME("update_time"),
		;

		private String columnName;

		private COLUMNS (String columnName)
		{
			this.columnName = columnName;
		}

		public void setColumnName (String columnName)
		{
			this.columnName = columnName;
		}

		public String getColumnName ()
		{
			return this.columnName;
		}

		public String getColumnAlias ()
		{
			return TABLE_ALIAS + "." + this.columnName;
		}

		public String getColumnAliasAsName ()
		{
			return TABLE_ALIAS  + "." + this.columnName + " as " + TABLE_ALIAS + "_" + this.columnName;
		}

		public String getColumnAliasName ()
		{
			return TABLE_ALIAS + "_" + this.columnName;
		}

	}

	public SiteUpdatesDB ()
	{

	}

	public static final RowMapper<SiteUpdates> ROW_MAPPER = new SiteUpdatesRowMapper ();
	public static final class  SiteUpdatesRowMapper implements RowMapper<SiteUpdates>
	{
		public SiteUpdates mapRow(ResultSet rs, int rowNum) throws SQLException 
		{
			SiteUpdates obj = new SiteUpdates();
			obj.setId(rs.getLong(COLUMNS.ID.getColumnName()));
			obj.setSite(rs.getString(COLUMNS.SITE.getColumnName()));
			obj.setDateAdded(rs.getTimestamp(COLUMNS.DATE_ADDED.getColumnName()));
			obj.setEventsNumber(rs.getInt(COLUMNS.EVENTS_NUMBER.getColumnName()));
			obj.setUpdateTime(rs.getInt(COLUMNS.UPDATE_TIME.getColumnName()));
			return obj;
		}
	}

	public static final RowUnmapper<SiteUpdates> ROW_UNMAPPER = new SiteUpdatesRowUnmapper ();
	public static final class SiteUpdatesRowUnmapper implements RowUnmapper<SiteUpdates>
	{
		public Map<String, Object> mapColumns(SiteUpdates siteupdates)
		{
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			//mapping.put(COLUMNS.ID.getColumnName(), siteupdates.getId());
			mapping.put(COLUMNS.SITE.getColumnName(), siteupdates.getSite());
			if (siteupdates.getDateAdded() != null)
				mapping.put(COLUMNS.DATE_ADDED.getColumnName(), new Timestamp (siteupdates.getDateAdded().getTime()));
			mapping.put(COLUMNS.EVENTS_NUMBER.getColumnName(), siteupdates.getEventsNumber());
			mapping.put(COLUMNS.UPDATE_TIME.getColumnName(), siteupdates.getUpdateTime());
			return mapping;
		}
	}

	public static final RowMapper<SiteUpdates> ALIAS_ROW_MAPPER = new SiteUpdatesAliasRowMapper ();
	public static final class  SiteUpdatesAliasRowMapper implements RowMapper<SiteUpdates>
	{
		public SiteUpdates mapRow(ResultSet rs, int rowNum) throws SQLException 
		{
			SiteUpdates obj = new SiteUpdates();
			obj.setId(rs.getLong(COLUMNS.ID.getColumnAliasName()));
			obj.setSite(rs.getString(COLUMNS.SITE.getColumnAliasName()));
			obj.setDateAdded(rs.getTimestamp(COLUMNS.DATE_ADDED.getColumnAliasName()));
			obj.setEventsNumber(rs.getInt(COLUMNS.EVENTS_NUMBER.getColumnAliasName()));
			obj.setUpdateTime(rs.getInt(COLUMNS.UPDATE_TIME.getColumnAliasName()));
			return obj;
		}
	}

	public static StringBuffer getAllColumnAliases ()
	{
		StringBuffer strBuf = new StringBuffer ();
		int i = COLUMNS.values ().length;
		for (COLUMNS c : COLUMNS.values ())
		{
			strBuf.append (c.getColumnAliasAsName ());
			if (--i > 0)
				strBuf.append (", ");
		}
		return strBuf;
	}

	/* START Do not remove/edit this line. CodeGenerator will preserve any code between start and end tags.*/

	/* END Do not remove/edit this line. CodeGenerator will preserve any code between start and end tags.*/

}