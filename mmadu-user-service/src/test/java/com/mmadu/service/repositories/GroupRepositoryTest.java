package com.mmadu.service.repositories;

import com.mmadu.service.config.DatabaseConfig;
import com.mmadu.service.entities.Group;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@Import(DatabaseConfig.class)
class GroupRepositoryTest {
    @Autowired
    private GroupRepository groupRepository;

    @Test
    void testTreePersistence() {
        Group ancestor = new Group();
        ancestor.setName("ancestor");
        ancestor.setDomainId("0");
        ancestor.setIdentifier("ancestor");
        ancestor.setDescription("ancestor");
        ancestor = groupRepository.save(ancestor);
        final Group readAncestor = ancestor;
        Group parent = new Group();
        parent.setDomainId("0");
        parent.setName("workers");
        parent.setDescription("people who work");
        parent.setIdentifier("workers");
        parent.setParent(ancestor);
        parent.setDomainId("0");
        parent = groupRepository.save(parent);
        groupRepository.save(ancestor);
        parent = groupRepository.save(parent);
        Group child = new Group();
        child.setName("doctors");
        child.setDescription("doctors");
        child.setIdentifier("doctors");
        child.setParent(parent);
        child.setDomainId("0");
        final Group child1 = groupRepository.save(child);
        Group child2 = new Group();
        child2.setName("lawyers");
        child2.setDescription("lawyers");
        child2.setIdentifier("lawyers");
        child2.setParent(parent);
        child2.setDomainId("0");
        Group child2Saved = groupRepository.save(child2);
        groupRepository.save(parent);

        Group readParent = groupRepository.findById(parent.getId()).get();
        assertAll(
                () -> assertEquals(readAncestor, readParent.getParent()),
                () -> assertEquals(Set.of(child1, child2Saved), readParent.getChildren())
        );
    }

}