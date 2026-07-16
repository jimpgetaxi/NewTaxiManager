# Κανονισμοί και Οργάνωση Project (AGENTS)

Αυτό το έγγραφο λειτουργεί ως η κεντρική μνήμη (Command Center) για το project `TaxiManager`. Καταγράφει τους κανόνες, τις τεχνολογίες και τους ρόλους.

## 1. Τεχνολογίες & Αρχιτεκτονική
- **Πλατφόρμα:** Native Android
- **Γλώσσα:** Kotlin
- **UI Toolkit:** Jetpack Compose (Material 3)
- **Αρχιτεκτονική:** MVVM (Model-View-ViewModel)
- **Βάση Δεδομένων:** Room Database (Offline first)
- **Dependency Injection:** Dagger Hilt
- **Navigation:** Jetpack Navigation Compose

## 2. Βασικοί Κανόνες (Rules of Engagement)
- **Έγκριση πριν από Push:** ΠΟΤΕ δεν κάνουμε `git push` αν δεν το ζητήσει ή δεν το εγκρίνει ρητά ο χρήστης (CEO). Οι αλλαγές γίνονται commit τοπικά.
- **Μηδενικά Warnings:** Όλα τα warnings κατά το build (π.χ. deprecated APIs) πρέπει να διορθώνονται άμεσα.
- **Cyberpunk UI:** Απαγορεύεται το λευκό φόντο (Light Mode). Η εφαρμογή τρέχει αποκλειστικά σε Dark Mode, με χρώματα Μαύρο/Γκρι, και neon πινελιές (Μωβ, Κίτρινο, Γαλάζιο). 
- **Λογική ΦΠΑ:** Το ΦΠΑ ορίζεται σταθερά στο 13% και υπολογίζεται ΠΑΝΤΑ πάνω στο ποσό της απόδειξης (όχι της είσπραξης). Τύπος: `(ReceiptAmount / 1.13) * 0.13`.

## 3. Διαθέσιμοι Πράκτορες (Subagents)
Για την καλύτερη δυνατή υλοποίηση του project, έχουμε στη διάθεσή μας εξειδικευμένους "πράκτορες" (subagents) που μπορούμε να καλέσουμε ανά πάσα στιγμή για βοήθεια σε συγκεκριμένους τομείς:
1. **Android_UI_Designer:** Ειδικός στο Jetpack Compose, υπεύθυνος για τα Animations, τη δημιουργία πολύπλοκων γραφημάτων και την τήρηση του Cyberpunk στυλ.
2. **Android_DB_Architect:** Ειδικός στο Room Database και στα SQL queries για πολύπλοκα στατιστικά εσόδων-εξόδων.
