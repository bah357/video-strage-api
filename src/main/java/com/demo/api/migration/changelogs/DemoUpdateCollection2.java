package com.demo.api.migration.changelogs;

import com.demo.api.domain.FileDomain;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@ChangeLog(order = "00002")
public class DemoUpdateCollection2 {

    @ChangeSet(order = "001", id = "test_002", author = "Bah")
    public void updateExistingDeliveryCacheData(MongockTemplate mongockTemplate) {
        var query = new Query(Criteria.where("arr").exists(true));
        var updateDefinition = new Update();
        updateDefinition.rename("arr[0].code", "arr[0].codeNumber");
        mongockTemplate.updateMulti(query, updateDefinition, FileDomain.class);
    }
}
