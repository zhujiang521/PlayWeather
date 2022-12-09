package model.weather

/**
 * 当天天气详情数据
 */
data class DayItemData(
    var title: String,
    var value: String,
    var tip: String,
    var titleDetails: String,
    var valueDetails: String
)