package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.person.Person;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
public class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_ASSIGNMENT = "Assignment list contains duplicate assignment(s).";
    public static final String MESSAGE_NO_SUCH_PERSON = "Persons list does not contain such person.";
    public static final String MESSAGE_NULL_PERSON = "Persons list contains null.";
    public static final String MESSAGE_NULL_ASSIGNMENT = "Assignment list contains null.";

    public static final String PERSONS_PROPERTY = "persons";
    public static final String ASSIGNMENTS_PROPERTY = "assignments";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedAssignment> assignments = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty(PERSONS_PROPERTY) List<JsonAdaptedPerson> persons,
                                       @JsonProperty(ASSIGNMENTS_PROPERTY) List<JsonAdaptedAssignment> assignments) {
        this.persons.addAll(persons);
        this.assignments.addAll(assignments);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
        assignments.addAll(source.getAssignmentList().stream().map(JsonAdaptedAssignment::new)
                        .collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            if (jsonAdaptedPerson == null) {
                throw new IllegalValueException(MESSAGE_NULL_PERSON);
            }

            Person person = jsonAdaptedPerson.toModelType();

            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(person);
        }
        for (JsonAdaptedAssignment jsonAdaptedAssignment: assignments) {
            if (jsonAdaptedAssignment == null) {
                throw new IllegalValueException(MESSAGE_NULL_ASSIGNMENT);
            }

            Assignment assignment = jsonAdaptedAssignment.toModelType();

            // check person and duplicate
            if (!addressBook.hasExactPerson(assignment.getPerson())) {
                throw new IllegalValueException(MESSAGE_NO_SUCH_PERSON);
            }
            if (addressBook.hasAssignment(assignment)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_ASSIGNMENT);
            }
            addressBook.addAssignment(assignment);
        }
        return addressBook;
    }
}
