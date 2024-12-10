package com.example.interventions

import java.time.LocalTime

class Engine : ArrayList<Intervention>() {
    fun insertInList(item: Intervention) {
        this.add(item)
//        var inserted = false
//        this.forEachIndexed { index, intervention ->
//            if (LocalTime.MAX != intervention.targetTime) {
//                if (intervention.targetTime.isAfter(item.targetTime)) {
//                    this.add(index, item)
//                    inserted = true
//                }
//            }
//            else {
//                this.add(item)
//                inserted = true
//            }
//        }
//        if (!inserted) {
//            this.add(item)
//        }
    }

}

fun planningEngine() : MutableList<Intervention> {
    val engine = Engine()

    interventionSource.forEach {
        engine.insertInList(it)
    }

    val result = engine.mapIndexed { index, it ->
        it.copy( id = index)
    }

    return result.toMutableList()
}

