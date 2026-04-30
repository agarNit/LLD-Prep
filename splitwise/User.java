package splitwise;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String email;
    private String name;
    private List<Expense> expenses;
    
    public User(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.expenses = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}