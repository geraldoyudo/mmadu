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
        parent = groupRepository.save(parent);
        groupRepository.save(ancestor);
        Group child = new Group();
        child.setName("doctors");
        child.setDescription("doctors");
        child.setIdentifier("doctors");
        child.setParent(parent);
        groupRepository.save(child);
        groupRepository.save(parent);

        Group readParent = groupRepository.findById(parent.getId()).get();
        assertAll(
                () -> assertEquals(readAncestor, readParent.getParent()),
                () -> assertEquals(Set.of(child), readParent.getChildren())
        );
    }

}