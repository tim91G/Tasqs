package com.timgortworst.roomy.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.timgortworst.roomy.model.Household
import com.timgortworst.roomy.utils.Constants
import com.timgortworst.roomy.utils.Constants.AGENDA_EVENT_CATEGORIES_COLLECTION_REF
import com.timgortworst.roomy.utils.GenerateData

class HouseholdRepository(db: FirebaseFirestore) {

    val householdsCollectionRef = db.collection(Constants.HOUSEHOLD_COLLECTION_REF)

    fun createNewHousehold(onComplete: (householdID: String) -> Unit, onFailure: () -> Unit) {
        val householdDocRef = householdsCollectionRef.document()
        val householdID = householdDocRef.id

        val categories = GenerateData.eventCategories()
        for (category in categories) {
            val householdSubEventCategories =
                householdDocRef.collection(AGENDA_EVENT_CATEGORIES_COLLECTION_REF).document()
            category.categoryId = householdSubEventCategories.id
            householdSubEventCategories.set(category)
        }

        householdDocRef.set(Household(householdId = householdID))
            .addOnCompleteListener {
                onComplete.invoke(householdID)
            }.addOnFailureListener {
                onFailure.invoke()
            }
    }

    fun removeHousehold(householdId: String) {
        householdsCollectionRef
            .document(householdId)
            .delete()
    }

    companion object {
        private const val TAG = "TIMTIM"
    }
}
