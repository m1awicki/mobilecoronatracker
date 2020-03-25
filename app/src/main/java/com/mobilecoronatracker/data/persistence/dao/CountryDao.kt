package com.mobilecoronatracker.data.persistence.dao

import androidx.room.Dao
import com.mobilecoronatracker.data.persistence.entity.Country

@Dao
abstract class CountryDao : BaseDao<Country> {

}
