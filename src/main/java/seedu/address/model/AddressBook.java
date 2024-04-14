package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.assignment.AssignmentDetails;
import seedu.address.model.assignment.AssignmentList;
import seedu.address.model.person.Email;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final AssignmentList assignments;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        assignments = new AssignmentList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the assignment list with {@code assignments}.
     */
    public void setAssignments(List<Assignment> assignments) {
        this.assignments.setAssignments(assignments);
    }

    /**
     * Cascade delete assignments when a person that has been assigned to has been deleted.
     *
     * @param before is a person to be deleted
     */
    public void cascadeDeleteAssignments(Person before, Person after) {
        requireNonNull(before);
        requireNonNull(after);

        List<Assignment> assignmentsToChange = new ArrayList<>();
        for (Assignment a : assignments) {
            if (a.getPerson().equals(before)) {
                assignmentsToChange.add(a);
            }
        }
        for (Assignment a : assignmentsToChange) {
            Assignment editedAssignment = new Assignment(after, new AssignmentDetails(a.getDetails()),
                    a.getAvailability());
            if (after.getAvailabilities().contains(a.getAvailability())) {
                this.assignments.setAssignment(a, editedAssignment);
            } else {
                this.assignments.remove(a);
            }
        }
    }

    /**
     * Cascade delete assignments when a person that has been assigned to has been deleted.
     *
     * @param toDelete is a person to be deleted
     */
    public void cascadeDeleteAssignments(Person toDelete) {
        requireNonNull(toDelete);

        List<Assignment> assignmentsToDelete = new ArrayList<>();
        for (Assignment a : assignments) {
            if (a.getPerson().equals(toDelete)) {
                assignmentsToDelete.add(a);
            }
        }
        for (Assignment a : assignmentsToDelete) {
            this.assignments.remove(a);
        }
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setAssignments(newData.getAssignmentList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasExactPerson(Person person) {
        requireNonNull(person);
        return persons.containsExactPerson(person);
    }

    /**
     * Returns true if a person with the same phone number as {@code person} exists in the address book.
     */
    public boolean hasPhone(Phone phone) {
        requireNonNull(phone);
        return persons.contains(phone);
    }

    /**
     * Returns true if a person with the same email as {@code person} exists in the address book.
     */
    public boolean hasEmail(Email email) {
        requireNonNull(email);
        return persons.contains(email);
    }

    /**
     * Returns true if an assignment with the same identity as {@code assignment} exists.
     */
    public boolean hasAssignment(Assignment assignment) {
        requireNonNull(assignment);
        return assignments.contains(assignment);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Adds an assignment to the application.
     */
    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }



    /**
     * Removes an assignment to the application.
     */
    public void removeAssignment(Assignment key) {
        assignments.remove(key);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);
        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Assignment> getAssignmentList() {
        return assignments.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
