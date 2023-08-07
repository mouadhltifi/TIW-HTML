package tiw.polimi.it.dao;

import tiw.polimi.it.beans.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class SellerDAO extends GeneralDAO{
    public SellerDAO(Connection conn) {
        super(conn);
    }

    public SellerDAO(Connection conn, String language, String country) {
        super(conn, language, country);
    }



}
