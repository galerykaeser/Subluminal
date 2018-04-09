package tech.subluminal.client.stores;

import tech.subluminal.shared.stores.IdentifiableCollection;
import tech.subluminal.shared.stores.records.User;
import tech.subluminal.shared.util.Synchronized;

import java.util.Collection;

public class UserCollection extends IdentifiableCollection<User> {

    public Synchronized<Collection<Synchronized<User>>> getByUsername (String name){
        return getWithPredicate(user -> user.getUsername().equals(name));
    }
}
