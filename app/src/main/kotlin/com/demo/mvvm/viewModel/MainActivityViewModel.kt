package com.demo.mvvm.viewModel

import com.demo.mvvm.model.ShapeParams
import com.demo.mvvm.view.activity.BaseActivityViewModel
import com.demo.mvvm.view.fragment.ShapeType
import com.demo.utils.SharedPreferencesHelper
import java.util.HashMap
import javax.inject.Inject

class MainActivityViewModel @Inject constructor() : BaseActivityViewModel() {

    var mapShapes: HashMap<ShapeType, ShapeParams>? = SharedPreferencesHelper.shapeTypeParam
         set(value) {
            SharedPreferencesHelper.shapeTypeParam = value
            field = value
        }

    init {
        if (mapShapes == null) {
            mapShapes = generateMapShapes()
        }
    }

    private fun generateMapShapes(): HashMap<ShapeType, ShapeParams> {
        val mapResult = HashMap<ShapeType, ShapeParams>()
        mapResult[ShapeType.Square] = ShapeParams()
        mapResult[ShapeType.Triangle] = ShapeParams()
        mapResult[ShapeType.Hexagon] = ShapeParams()
        return mapResult
    }

    fun updateMap(shapeType: ShapeType, params: ShapeParams) {
        mapShapes?.put(shapeType, params)
        SharedPreferencesHelper.shapeTypeParam = mapShapes
    }
}
