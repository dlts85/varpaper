package com.github.bioinfo.webes.dao.account;

import java.util.List;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.github.bioinfo.webes.entity.Account;
import com.github.bioinfo.webes.utils.EsScrollUtil;

@Repository
public class AccountDaoImp implements AccountDao {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EsScrollUtil esUtil;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public Account findAccount(String name) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.termQuery("_id", name))
				.build();
		
		List<Account> list = esUtil.queryForList(searchQuery, Account.class);
		
		if(1 == list.size()) {
			return list.get(0);
		}
		
		return null;
	}

	public void save(Account account) {
		account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
		account.setPasswordConfirm(bCryptPasswordEncoder.encode(account.getPasswordConfirm()));

		IndexQuery indexQuery = new IndexQueryBuilder()
				.withId(account.getUserName())
				.withIndexName("account")
				.withObject(account)
				.withType("account_list")
				.build();
		esUtil.index(indexQuery);
		
	}

}
