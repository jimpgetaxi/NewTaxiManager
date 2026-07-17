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
- **Έγκριση πριν από Push:** Όταν ολοκληρώνεται και επιβεβαιώνεται μια φάση, κάνουμε πάντα `git commit` και `git push` στο αποθετήριο: `https://github.com/jimpgetaxi/NewTaxiManager`.
- **Μηδενικά Warnings (Local Debug):** Πάντα τρέχουμε `.\gradlew.bat compileDebugKotlin` ή `assembleDebug` τοπικά για να ελέγχουμε αν ο κώδικας κάνει compile και για να διορθώνουμε τυχόν warnings *πριν* τον παραδώσουμε στον χρήστη.
- **Cyberpunk UI & UX:** Απαγορεύεται το λευκό φόντο (Light Mode). Η εφαρμογή τρέχει αποκλειστικά σε Dark Mode. Στο μέλλον πρέπει να βελτιωθεί η δομή του UI (Glassmorphism, glowing borders, custom charts) ώστε να ταιριάζει απόλυτα στα αρχικά mockups, και όχι απλά να έχει νέον χρώματα.
- **Γλώσσα & Localization:** Το UI πρέπει να είναι *πλήρως στα Ελληνικά* (επειδή ο χρήστης τη χρησιμοποιεί ο ίδιος), ΑΛΛΑ πρέπει πάντα να χρησιμοποιείται το `strings.xml` ώστε μελλοντικά να μπορεί να προστεθεί υποστήριξη Αγγλικών για εμπορική εκμετάλλευση.
- **Λογική ΦΠΑ:** Το ΦΠΑ ορίζεται σταθερά στο 13% και υπολογίζεται ΠΑΝΤΑ πάνω στο ποσό της απόδειξης (όχι της είσπραξης). Τύπος: `(ReceiptAmount / 1.13) * 0.13`.

## 3. Διαθέσιμοι Πράκτορες (Subagents)
Για την καλύτερη δυνατή υλοποίηση του project, έχουμε στη διάθεσή μας εξειδικευμένους "πράκτορες" (subagents) που μπορούμε να καλέσουμε ανά πάσα στιγμή για βοήθεια σε συγκεκριμένους τομείς:
1. **Android_UI_Designer:** Ειδικός στο Jetpack Compose, υπεύθυνος για τα Animations, τη δημιουργία πολύπλοκων γραφημάτων και την τήρηση του Cyberpunk στυλ.
2. **Android_DB_Architect:** Ειδικός στο Room Database και στα SQL queries για πολύπλοκα στατιστικά εσόδων-εξόδων.

## 4. Κατάσταση Project & Μελλοντικά Βήματα (Roadmap)
**Τρέχουσα Κατάσταση (Ολοκληρώθηκε Φάση 2):** Έχουμε στήσει το Data Layer (Rides & Expenses) με Room Database, MVVM architecture, και το βασικό Dashboard που υπολογίζει το Net Profit. Υπάρχει σύστημα προσθήκης Εξόδων με custom κατηγορίες.
**Επόμενα Βήματα (Τι θα κάνουμε αύριο/στη συνέχεια):**
1. **Πλήρης Εξελληνισμός (Localization):** Μεταφορά όλων των "hardcoded" αγγλικών κειμένων (π.χ. "TOTAL REVENUE", "ADD RIDE") στο `strings.xml` και μετάφρασή τους στα Ελληνικά.
2. **UI/UX Overhaul:** Βαθιά αναβάθμιση του σχεδιασμού (shapes, γραφικά, shadows) για να μοιάζει στα επαγγελματικά UI Proposals που είχαν συμφωνηθεί αρχικά.
3. **Σύστημα "Βάρδιας" (Shift Management):** Λογική Start/End Shift για ομαδοποίηση των κουρσών ανά βάρδια και υπολογισμό στατιστικών ωριαίου κέρδους.
4. **Οπτικά Γραφήματα (Visual Analytics):** Gauge/Bar charts για απεικόνιση των εσόδων έναντι του στόχου.
