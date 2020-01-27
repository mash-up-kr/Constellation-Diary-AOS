package com.mashup.telltostar.util

import android.content.res.Resources
import com.mashup.telltostar.R

object ConstellationUtil {

    fun getDate(resource: Resources, constellation: String) = when (constellation) {
        resource.getString(R.string.goat) -> resource.getString(R.string.date_goat)
        resource.getString(R.string.waterbottle) -> resource.getString(R.string.date_waterbottle)
        resource.getString(R.string.fish) -> resource.getString(R.string.date_fish)
        resource.getString(R.string.yang) -> resource.getString(R.string.date_yang)
        resource.getString(R.string.hwangso) -> resource.getString(R.string.date_hwangso)
        resource.getString(R.string.twin) -> resource.getString(R.string.date_twin)
        resource.getString(R.string.crab) -> resource.getString(R.string.date_crab)
        resource.getString(R.string.lion) -> resource.getString(R.string.date_lion)
        resource.getString(R.string.girl) -> resource.getString(R.string.date_girl)
        resource.getString(R.string.chunching) -> resource.getString(R.string.date_chunching)
        resource.getString(R.string.jungal) -> resource.getString(R.string.date_jungal)
        resource.getString(R.string.gungso) -> resource.getString(R.string.date_gungso)
        else -> resource.getString(R.string.date_goat)
    }

    fun getIconBlack(resource: Resources, constellation: String) = when (constellation) {
        resource.getString(R.string.goat) -> R.drawable.star_goat
        resource.getString(R.string.waterbottle) -> R.drawable.star_waterbottle
        resource.getString(R.string.fish) -> R.drawable.star_fish
        resource.getString(R.string.yang) -> R.drawable.star_yang
        resource.getString(R.string.hwangso) -> R.drawable.star_hwangso
        resource.getString(R.string.twin) -> R.drawable.star_twin
        resource.getString(R.string.crab) -> R.drawable.star_crab
        resource.getString(R.string.lion) -> R.drawable.star_lion
        resource.getString(R.string.girl) -> R.drawable.star_girl
        resource.getString(R.string.chunching) -> R.drawable.star_chunching
        resource.getString(R.string.jungal) -> R.drawable.star_jungal
        resource.getString(R.string.gungso) -> R.drawable.star_gungso
        else -> R.drawable.star_goat
    }

    fun getIcon(resource: Resources, constellation: String) = when (constellation) {
        resource.getString(R.string.goat) -> R.drawable.star_ic_goat
        resource.getString(R.string.waterbottle) -> R.drawable.star_ic_waterbottle
        resource.getString(R.string.fish) -> R.drawable.star_ic_fish
        resource.getString(R.string.yang) -> R.drawable.star_ic_yang
        resource.getString(R.string.hwangso) -> R.drawable.star_ic_hwangso
        resource.getString(R.string.twin) -> R.drawable.star_ic_twin
        resource.getString(R.string.crab) -> R.drawable.star_ic_crab
        resource.getString(R.string.lion) -> R.drawable.star_ic_lion
        resource.getString(R.string.girl) -> R.drawable.star_ic_girl
        resource.getString(R.string.chunching) -> R.drawable.star_ic_chunching
        resource.getString(R.string.jungal) -> R.drawable.star_ic_jungal
        resource.getString(R.string.gungso) -> R.drawable.star_ic_gungso
        else -> R.drawable.star_ic_goat
    }


    fun getImg(resource: Resources, constellation: String) = when (constellation) {
        resource.getString(R.string.goat) -> R.drawable.star_img_goat
        resource.getString(R.string.waterbottle) -> R.drawable.star_img_waterbottle
        resource.getString(R.string.fish) -> R.drawable.star_img_fish
        resource.getString(R.string.yang) -> R.drawable.star_img_yang
        resource.getString(R.string.hwangso) -> R.drawable.star_img_hwangso
        resource.getString(R.string.twin) -> R.drawable.star_img_twin
        resource.getString(R.string.crab) -> R.drawable.star_img_crab
        resource.getString(R.string.lion) -> R.drawable.star_img_lion
        resource.getString(R.string.girl) -> R.drawable.star_img_girl
        resource.getString(R.string.chunching) -> R.drawable.star_img_chunching
        resource.getString(R.string.jungal) -> R.drawable.star_img_jungal
        resource.getString(R.string.gungso) -> R.drawable.star_img_gungso
        else -> R.drawable.star_img_goat
    }

    fun getConstellations(resource: Resources) = listOf<String>(
        resource.getString(R.string.goat),
        resource.getString(R.string.waterbottle),
        resource.getString(R.string.fish),
        resource.getString(R.string.yang),
        resource.getString(R.string.hwangso),
        resource.getString(R.string.twin),
        resource.getString(R.string.crab),
        resource.getString(R.string.lion),
        resource.getString(R.string.girl),
        resource.getString(R.string.chunching),
        resource.getString(R.string.jungal),
        resource.getString(R.string.gungso)
    )
}