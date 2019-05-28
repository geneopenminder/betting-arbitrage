package com.isfootball.jdbc.rep.db;

import java.sql.SQLException;

import com.isfootball.jdbc.Bet;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;
import com.nurkiewicz.jdbcrepository.RowUnmapper;

import java.sql.Timestamp;

/**
 * This class is generated by Spring Data Jdbc code generator.
 *
 * @author Spring Data Jdbc Code Generator
 */
public class BetsDB
{

	private static String TABLE_NAME = "BETS";

	private static String TABLE_ALIAS = "bets";

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
		MATCH_ID("match_id"),
		BET("bet"),
		BET_VAL("bet_val"),
		SITE("site"),
		DATE_ADDED("date_added"),
		UPDATED("updated"),
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

	public BetsDB ()
	{

	}

	public static final RowMapper<Bet> ROW_MAPPER = new BetsRowMapper ();
	public static final class  BetsRowMapper implements RowMapper<Bet>
	{
		public Bet mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Bet obj = new Bet();
			obj.setId(rs.getLong(COLUMNS.ID.getColumnName()));
			obj.setMatchId(rs.getLong(COLUMNS.MATCH_ID.getColumnName()));
			obj.setBet(rs.getString(COLUMNS.BET.getColumnName()));
			obj.setBetVal(rs.getDouble(COLUMNS.BET_VAL.getColumnName()));
			obj.setSite(rs.getString(COLUMNS.SITE.getColumnName()));
			obj.setDateAdded(rs.getTimestamp(COLUMNS.DATE_ADDED.getColumnName()));
			obj.setUpdated(rs.getTimestamp(COLUMNS.UPDATED.getColumnName()));
			return obj;
		}
	}

	public static final RowUnmapper<Bet> ROW_UNMAPPER = new BetsRowUnmapper ();
	public static final class BetsRowUnmapper implements RowUnmapper<Bet>
	{
		public Map<String, Object> mapColumns(Bet bets)
		{
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			//mapping.put(COLUMNS.ID.getColumnName(), bets.getId());
			mapping.put(COLUMNS.MATCH_ID.getColumnName(), bets.getMatchId());
			mapping.put(COLUMNS.BET.getColumnName(), bets.getBet());
			mapping.put(COLUMNS.BET_VAL.getColumnName(), bets.getBetVal());
			mapping.put(COLUMNS.SITE.getColumnName(), bets.getSite());
			if (bets.getDateAdded() != null)
				mapping.put(COLUMNS.DATE_ADDED.getColumnName(), new Timestamp (bets.getDateAdded().getTime()));
			if (bets.getUpdated() != null)
				mapping.put(COLUMNS.UPDATED.getColumnName(), new Timestamp (bets.getUpdated().getTime()));
			return mapping;
		}
	}

	public static final RowMapper<Bet> ALIAS_ROW_MAPPER = new BetsAliasRowMapper ();
	public static final class  BetsAliasRowMapper implements RowMapper<Bet>
	{
		public Bet mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			Bet obj = new Bet();
			obj.setId(rs.getLong(COLUMNS.ID.getColumnAliasName()));
			obj.setMatchId(rs.getLong(COLUMNS.MATCH_ID.getColumnAliasName()));
			obj.setBet(rs.getString(COLUMNS.BET.getColumnAliasName()));
			obj.setBetVal(rs.getDouble(COLUMNS.BET_VAL.getColumnAliasName()));
			obj.setSite(rs.getString(COLUMNS.SITE.getColumnAliasName()));
			obj.setDateAdded(rs.getTimestamp(COLUMNS.DATE_ADDED.getColumnAliasName()));
			obj.setUpdated(rs.getTimestamp(COLUMNS.UPDATED.getColumnAliasName()));
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