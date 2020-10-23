package org.natc.app.util;

import org.natc.app.entity.domain.FirstName;
import org.natc.app.entity.domain.LastName;
import org.natc.app.repository.FirstNameRepository;
import org.natc.app.repository.LastNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TestHelpers {

    @Autowired
    private FirstNameRepository firstNameRepository;

    @Autowired
    private LastNameRepository lastNameRepository;

    public void seedFirstAndLastNames() {
        final List<FirstName> firstNames = new ArrayList<>();
        final List<LastName> lastNames = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            final String name = generateRandomString(5, 12);
            final FirstName firstName = FirstName.builder().name(name).frequency(1.0).build();
            firstNames.add(firstName);
        }

        for (int i = 0; i < 100; i++) {
            final String name = generateRandomString(5, 12);
            final LastName lastName = LastName.builder().name(name).frequency(1.0).build();
            lastNames.add(lastName);
        }

        firstNameRepository.saveAll(firstNames);
        lastNameRepository.saveAll(lastNames);
    }

    private String generateRandomString(final Integer minLength, final Integer maxLength) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ";
        final StringBuilder builder = new StringBuilder();
        final int nameLength = minLength + (int)(Math.random() * (maxLength - minLength));

        while (builder.length() < nameLength) {
            builder.append(chars.charAt((int)(Math.random() * chars.length())));
        }

        return builder.toString();
    }
}
