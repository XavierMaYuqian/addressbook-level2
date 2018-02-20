package seedu.addressbook.commands;

import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.*;
import seedu.addressbook.data.tag.Tag;
import seedu.addressbook.data.tag.UniqueTagList;

import java.util.HashSet;
import java.util.Set;

/**
 * Modify a person's information that is already in the address book.
 */
public class ModifyCommand extends Command{
    public static final String COMMAND_WORD = "modify";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Modify a person in the address book. "
            +"Contact details can be marked private by prepending 'p' to the prefix.\n"
            +"Parameters: NAME [p]p/PHONE [p]e/EMAIL [p]a/ADDRESS  [t/TAG]...\n"
            +"Example: " + COMMAND_WORD
            +" John Doe p/98765432 e/johnd@gmail.com a/311, Clementi Ave 2, #02-25 t/friends t/owesMoney";

    public static final String MESSAGE_SUCCESS = COMMAND_WORD + " command executed! %s has been modified successfully!";
    public static final String MESSAGE_PERSON_NOT_FOUND = COMMAND_WORD + " failed! Person: %s does not exist at all."
            +" Please use the add command to add this person.";
    public static final String MESSAGE_EDIT_FAILURE = "Wrong person was modified! Removed: %s for %s";
    private final Person thePersonToBeModified;

    /**
     *
     * Class constructor.
     */
    public ModifyCommand(String name, String phone, boolean isPhonePrivate, String email, boolean isEmailPrivate, String address, boolean isAddressPrivate, Set<String> tags) throws IllegalValueException {
        final Set<Tag> tagSet = new HashSet<>();

        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        this.thePersonToBeModified = new Person(new Name(name), new Phone(phone, isPhonePrivate), new Email(email, isEmailPrivate), new Address(address, isAddressPrivate), new UniqueTagList(tagSet));

    }

    /**
     * Finds a matching name from the address book
     * @param fullName Name to be searched for in address book
     * @return Person that matches name given
     */
    private ReadOnlyPerson findThePerson(String fullName){
        ReadOnlyPerson personToBeModified = null;
        UniquePersonList everyone = addressBook.getAllPersons();
        for(ReadOnlyPerson person : everyone ){
            if(person.getName().fullName.equals(fullName)){
                personToBeModified = person;
                break;
            }
        }
        return personToBeModified;
    }

    @Override
    public CommandResult execute() {
        ReadOnlyPerson personToBeModified = findThePerson(thePersonToBeModified.getName().fullName);
        if (personToBeModified == null){
            return new CommandResult(String.format( MESSAGE_PERSON_NOT_FOUND, thePersonToBeModified.getName()));
        }
        else{
            try {
                addressBook.removePerson(personToBeModified);
                addressBook.addPerson(thePersonToBeModified);
                return new CommandResult(String.format( MESSAGE_SUCCESS, thePersonToBeModified.getName()));
            } catch(Exception e){
                return new CommandResult(String.format(MESSAGE_EDIT_FAILURE, personToBeModified.getName(), thePersonToBeModified.getName()));
            }

        }

    }

}
