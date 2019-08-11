package com.timgortworst.roomy.presentation.base.di

import com.timgortworst.roomy.presentation.features.category.view.CategoryEditActivity
import com.timgortworst.roomy.presentation.features.category.view.CategoryEditView
import dagger.Binds
import dagger.Module

@Module
abstract class CategoryEditModule {
    @Binds
    internal abstract fun provideCategoryEditView(editTaskActivity: CategoryEditActivity): CategoryEditView
}
