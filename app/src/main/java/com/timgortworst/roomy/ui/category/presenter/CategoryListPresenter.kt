package com.timgortworst.roomy.ui.category.presenter

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.timgortworst.roomy.model.Category
import com.timgortworst.roomy.repository.CategoryRepository
import com.timgortworst.roomy.ui.category.view.CategoryListView
import com.timgortworst.roomy.utils.CoroutineLifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CategoryListPresenter(
        val view: CategoryListView,
        private val categoryRepository: CategoryRepository
) : CategoryRepository.CategoryListener, DefaultLifecycleObserver {

    private val scope = CoroutineLifecycleScope(Dispatchers.Main)

    init {
        if (view is LifecycleOwner) {
            view.lifecycle.addObserver(scope)
        }
    }

    fun listenToCategories() = scope.launch {
        categoryRepository.listenToCategories(this@CategoryListPresenter)
    }

    fun detachCategoryListener() {
        categoryRepository.detachCategoryListener()
    }

    fun deleteCategory(agendaEventCategory: Category) = scope.launch {
        categoryRepository.deleteCategoryForHousehold(agendaEventCategory)
    }

    override fun categoryAdded(category: Category) {
        view.presentNewCategory(category)
    }

    override fun categoryModified(category: Category) {
        view.presentEditedCategory(category)
    }

    override fun categoryDeleted(category: Category) {
        view.presentDeletedCategory(category)
    }

    override fun categoryLoading(isLoading: Boolean) {
        view.showLoadingState(isLoading)
    }
}
