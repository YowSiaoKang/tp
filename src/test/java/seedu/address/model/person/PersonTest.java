package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_AVAILABILITY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_ELDERCARE;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
        assertThrows(UnsupportedOperationException.class, () -> person.getAvailabilities().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(DANIEL.isSamePerson(DANIEL));

        // null -> returns false
        assertFalse(DANIEL.isSamePerson(null));

        // same name, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(DANIEL).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAvailabilities(VALID_AVAILABILITY_BOB).withTags(VALID_TAG_ELDERCARE).build();
        assertTrue(DANIEL.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(DANIEL).withName(VALID_NAME_BOB).build();
        assertFalse(DANIEL.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(DANIEL).build();
        assertTrue(DANIEL.equals(aliceCopy));

        // same object -> returns true
        assertTrue(DANIEL.equals(DANIEL));

        // null -> returns false
        assertFalse(DANIEL.equals(null));

        // different type -> returns false
        assertFalse(DANIEL.equals(5));

        // different person -> returns false
        assertFalse(DANIEL.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(DANIEL).withName(VALID_NAME_BOB).build();
        assertFalse(DANIEL.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(DANIEL).withPhone(VALID_PHONE_BOB).build();
        assertFalse(DANIEL.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(DANIEL).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(DANIEL.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(DANIEL).withAvailabilities(VALID_AVAILABILITY_BOB).build();
        assertFalse(DANIEL.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(DANIEL).withTags(VALID_TAG_ELDERCARE).build();
        assertFalse(DANIEL.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + DANIEL.getName() + ", phone=" + DANIEL.getPhone()
                + ", email=" + DANIEL.getEmail() + ", availabilities=" + DANIEL.getAvailabilities() + ", tags="
                + DANIEL.getTags() + "}";
        assertEquals(expected, DANIEL.toString());
    }
}
