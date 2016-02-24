package com.teamtreehouse.contactmgr;

import com.teamtreehouse.contactmgr.model.Contact;
import com.teamtreehouse.contactmgr.model.Contact.ContactBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class Application {
    // Hold a reusable reference to a SessionFactory (since we need only one)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        // Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Contact contact = new ContactBuilder("Chris","Ramacciotti")
                .withEmail("rama@teamtreehouse.com")
                .withPhone(7735556666L)
                .build();
        int id = save(contact);

        // Display a list of contacts before the update
        System.out.printf("%n%nBefore update%n%n");
        fetchAllContacts().stream().forEach(System.out::println);

        // Get the persisted contact
        Contact c = findContactById(id);

        // Update the contact
        c.setFirstName("Beatrix");

        // Persist the changes
        System.out.printf("%nUpdating...%n");
        update(c);
        System.out.printf("%nUpdate complete!%n");

        // Display a list of contacts after the update
        System.out.printf("%nAfter update%n");
        fetchAllContacts().stream().forEach(System.out::println);

        // Get the contact with id of 1
        c = findContactById(1);

        // Delete the contact
        System.out.printf("%nDeleting...%n");
        delete(c);
        System.out.printf("%nDeleted!%n");
        System.out.printf("%nAfter delete%n");
        fetchAllContacts().stream().forEach(System.out::println);
    }

    private static Contact findContactById(int id) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Retrieve the persistent object (or null if not found)
        Contact contact = session.get(Contact.class,id);

        // Close the session
        session.close();

        // Return the object
        return contact;
    }

    private static void delete(Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to update the contact
        session.delete(contact);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();
    }

    private static void update(Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to update the contact
        session.update(contact);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();
    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts() {
        // Open a session
        Session session = sessionFactory.openSession();

        // Create Criteria
        Criteria criteria = session.createCriteria(Contact.class);

        // Get a list of Contact objects according to the Criteria object
        List<Contact> contacts = criteria.list();

        // Close the session
        session.close();

        return contacts;
    }

    private static int save(Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to save the contact
        int id = (int)session.save(contact);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();

        return id;
    }
}
