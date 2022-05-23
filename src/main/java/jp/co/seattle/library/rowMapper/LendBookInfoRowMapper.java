package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.LendBookInfo;


@Configuration
public class LendBookInfoRowMapper implements RowMapper<LendBookInfo>{
	
	@Override
    public LendBookInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Query結果（ResultSet rs）を、オブジェクトに格納する実装
        LendBookInfo lendBookInfo = new LendBookInfo();

        // bookInfoの項目と、取得した結果(rs)のカラムをマッピングする
        lendBookInfo.setBookId(rs.getInt("book_id"));
        lendBookInfo.setTitle(rs.getString("title"));
        lendBookInfo.setRentDate(rs.getString("rent_date"));
        lendBookInfo.setReturnDate(rs.getString("return_date"));
        return lendBookInfo;
    }

}
