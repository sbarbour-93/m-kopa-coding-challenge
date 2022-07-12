package com.scottbarbour.devicelocktimer.data

import com.scottbarbour.devicelocktimer.data.model.CountryIsoCode

class CountryIsoCodeDataSource {
    suspend fun getCountryIsoCode() : CountryIsoCode {
        return CountryIsoCode.UG
    }
}