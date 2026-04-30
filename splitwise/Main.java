package splitwise;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        // -------------------------------------------------------
        // STEP 1 — Initialize the service
        // Always start with the top level orchestrator
        // This is your entry point for everything
        // -------------------------------------------------------
        SplitwiseService service = new SplitwiseService();


        // -------------------------------------------------------
        // STEP 2 — Create users
        // Create all entities that have no dependencies first
        // User has no dependencies — so create users first
        // Same order as bottom-up implementation
        // -------------------------------------------------------
        User alice   = new User("u1", "Alice",   "alice@gmail.com");
        User bob     = new User("u2", "Bob",     "bob@gmail.com");
        User charlie = new User("u3", "Charlie", "charlie@gmail.com");

        service.addUser(alice);
        service.addUser(bob);
        service.addUser(charlie);


        // -------------------------------------------------------
        // STEP 3 — Create group
        // Group depends on users — so create after users
        // Don't add members yet — that's a separate operation
        // -------------------------------------------------------
        Group goaTrip = new Group("g1", "Goa Trip");
        service.createGroup(goaTrip);


        // -------------------------------------------------------
        // STEP 4 — Add members to group
        // Always add members before adding expenses
        // Otherwise the group has no members to split among
        // -------------------------------------------------------
        service.addMember("g1", alice);
        service.addMember("g1", bob);
        service.addMember("g1", charlie);


        // -------------------------------------------------------
        // STEP 5 — Add group expense: Equal Split
        // Alice pays ₹300 for dinner
        // Split equally among all three — each owes ₹100
        //
        // Things to think about when adding any expense:
        // 1. Who paid?
        // 2. How much?
        // 3. Who is splitting?
        // 4. What split type?
        // 5. Is it a group or personal expense?
        // -------------------------------------------------------
        Expense dinner = new Expense(
                "Dinner",                               // description
                300,                                    // amount
                alice,                                  // paidBy
                "g1",                                   // groupId
                new EqualSplit(),                       // splitStrategy
                Arrays.asList(alice, bob, charlie)      // splitAmong
        );
        service.addExpense(dinner);
        // Expected ledger state after dinner:
        // Bob   owes Alice ₹100 [g1]
        // Charlie owes Alice ₹100 [g1]


        // -------------------------------------------------------
        // STEP 6 — Add group expense: Percent Split
        // Bob pays ₹200 for cab
        // Alice 50%, Bob 30%, Charlie 20%
        //
        // Note: percentages must sum to 100
        // Note: map key is userId not user name
        // -------------------------------------------------------
        Map<String, Double> cabPercentages = new HashMap<>();
        cabPercentages.put("u1", 50.0);   // Alice owes 50%
        cabPercentages.put("u2", 30.0);   // Bob owes 30% (payer — skipped)
        cabPercentages.put("u3", 20.0);   // Charlie owes 20%

        Expense cab = new Expense(
                "Cab",
                200,
                bob,
                "g1",
                new PercentSplit(cabPercentages),
                Arrays.asList(alice, bob, charlie)
        );
        service.addExpense(cab);
        // Expected ledger state after cab:
        // Alice owes Bob ₹100 [g1]   (50% of 200)
        // Charlie owes Bob ₹40 [g1]  (20% of 200)


        // -------------------------------------------------------
        // STEP 7 — Add group expense: Exact Split
        // Charlie pays ₹240 for groceries
        // Alice ₹100, Bob ₹80, Charlie ₹60
        //
        // Note: exact amounts must sum to total (100+80+60 = 240)
        // Note: map key is userId
        // -------------------------------------------------------
        Map<String, Double> groceryAmounts = new HashMap<>();
        groceryAmounts.put("u1", 100.0);  // Alice owes ₹100
        groceryAmounts.put("u2", 80.0);   // Bob owes ₹80
        groceryAmounts.put("u3", 60.0);   // Charlie owes ₹60 (payer — skipped)

        Expense groceries = new Expense(
                "Groceries",
                240,
                charlie,
                "g1",
                new ExactSplit(groceryAmounts),
                Arrays.asList(alice, bob, charlie)
        );
        service.addExpense(groceries);
        // Expected ledger state after groceries:
        // Alice owes Charlie ₹100 [g1]
        // Bob owes Charlie ₹80 [g1]


        // -------------------------------------------------------
        // STEP 8 — Add personal expense outside group
        // Alice pays ₹500 for a gift
        // Split equally between Alice and Bob only
        // groupId = null → becomes PERSONAL in service
        //
        // Key thing to test — personal expenses work independently
        // of any group
        // -------------------------------------------------------
        Expense gift = new Expense(
                "Gift",
                500,
                alice,
                null,                           // no group — personal
                new EqualSplit(),
                Arrays.asList(alice, bob)       // only Alice and Bob
        );
        service.addExpense(gift);
        // Expected ledger state after gift:
        // Bob owes Alice ₹250 [personal]


        // -------------------------------------------------------
        // STEP 9 — Show balances before settlement
        // Always show before and after settlement
        // so you can verify the numbers changed correctly
        // -------------------------------------------------------
        System.out.println("\n=============================");
        System.out.println("BALANCES BEFORE SETTLEMENT");
        System.out.println("=============================");

        service.showGlobalBalance("u1");   // Alice
        service.showGlobalBalance("u2");   // Bob
        service.showGlobalBalance("u3");   // Charlie

        System.out.println("\n--- Group balances: Goa Trip ---");
        service.showGroupBalance("u1", "g1");
        service.showGroupBalance("u2", "g1");
        service.showGroupBalance("u3", "g1");


        // -------------------------------------------------------
        // STEP 10 — Test removeMember with pending balance
        // Should throw exception — Bob has pending dues in g1
        // Always test your guard clauses explicitly
        // -------------------------------------------------------
        System.out.println("\n=============================");
        System.out.println("TEST — Remove member with pending balance");
        System.out.println("=============================");
        try {
            service.removeMember("g1", bob);   // should throw
        } catch (IllegalStateException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }


        // -------------------------------------------------------
        // STEP 11 — Settle payments
        // Test both group level and personal level settlement
        //
        // Things to think about when settling:
        // 1. Who is paying whom?
        // 2. How much?
        // 3. Is it group or personal?
        // -------------------------------------------------------
        System.out.println("\n=============================");
        System.out.println("SETTLEMENTS");
        System.out.println("=============================");

        // Bob settles with Alice in group g1
        Payment bobPaysAliceGroup = new Payment(
                bob,        // paidBy
                alice,      // paidTo
                100,        // amount
                "g1"        // groupId
        );
        service.settle(bobPaysAliceGroup);

        // Bob settles personal expense with Alice
        Payment bobPaysAlicePersonal = new Payment(
                bob,
                alice,
                250,
                SplitwiseService.PERSONAL   // personal settlement
        );
        service.settle(bobPaysAlicePersonal);

        // Charlie settles with Alice in group g1
        Payment charliePaysAlice = new Payment(
                charlie,
                alice,
                100,
                "g1"
        );
        service.settle(charliePaysAlice);


        // -------------------------------------------------------
        // STEP 12 — Show balances after settlement
        // Verify numbers updated correctly
        // -------------------------------------------------------
        System.out.println("\n=============================");
        System.out.println("BALANCES AFTER SETTLEMENT");
        System.out.println("=============================");

        service.showGlobalBalance("u1");
        service.showGlobalBalance("u2");
        service.showGlobalBalance("u3");


        // -------------------------------------------------------
        // STEP 13 — Now try removing Bob after settlement
        // Bob settled with Alice — but may still owe Charlie
        // Should still block if any dues remain
        // -------------------------------------------------------
        System.out.println("\n=============================");
        System.out.println("TEST — Remove Bob after partial settlement");
        System.out.println("=============================");
        try {
            service.removeMember("g1", bob);
        } catch (IllegalStateException e) {
            System.out.println("Still has dues: " + e.getMessage());
        }
    }
}