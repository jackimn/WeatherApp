package com.example.walkingpark.constants

import com.example.walkingpark.R

enum class Enums() {}

/**
 *   GoogleMaps 의 GeoCoder 의 주소변환 기능 로직 수행을 위한 Enum 클래스
 **/
enum class ADDRESS(
    val text: Char
) {
    DO('도'),
    SI('시'),
    GU('구'),
    GUN('군'),
    EUP('읍'),
    MUN('면'),
    DONG('동')
}

/**
 *   동네예보 조회 API 에서 받아온 데이터를 파싱하기 위한 Enum 클래스
 **/
enum class WEATHER(val code: String, desc: String) {
    RAIN_CHANCE("POP", "강수확률"),
    RAIN_TYPE("PTY", "강수타입"),
    HUMIDITY("REH", "습도"),
    SNOW("SNO", "강설"),
    SKY("SKY", "하늘상태"),
    TEMPERATURE("TMP", "기온"),
    TEMPERATURE_LOW("TMN", "최저기온"),
    TEMPERATURE_HIGH("TMX", "최고기온"),
    WIND_SPEED("WSD", "풍속"),
    WIND_SPEED_EW("UUU", "동서풍속"),
    WIND_SPEED_NS("VVV", "남북풍속"),
    WAVE_HEIGHT("VEC", "파도높이")
}

/**
 *
 */
enum class SKY(val index: Int, val text: String) {

    CLEAR(1, "맑음"),
    CLOUDY(3, "구름"),
    OVERCAST(4, "흐림"),
}

/**
 *
 */
enum class RAIN(val index: Int, val text: String) {
    NONE(0, "없음"),
    RAINY(1, "비"),
    RAIN_SNOW(2, "눈/비"),
    SNOW(3, "눈"),
    SHOWER(4, "소나기")
}

/**
 *   미세먼지 API 에서 받아온 데이터를 파싱하기 위한 Enum 클래스
 **/
enum class AIR(val code: String, val desc: String) {
    CO_VALUE("coValue", "일산화탄소 농도"),
    O3_VALUE("o3Value", "오존 농도"),
    NO2_VALUE("no2Value", "이산화질소 농도"),
    PM10("pm10Value", "미세먼지 PM10 농도"),
    PM10_24HOUR("pm10Value", "미세먼지 PM10 24시간 예측 농도"),
    PM25("pm25Value24", "미세먼지 PM2.5 농도"),
    PM25_24HOUR("pm25Value24", "미세먼지 24시간 예측 농도"),
    TOTAL_VALUE("khaiValue", "통합 대기환경 수치"),
    TOTAL_GRADE("khaiGrade", "통합 대기환경 지수"),
    GRADE_AH_WHANG_SAN("so2Grade", "아황산가스 지수"),
    GRADE_IL_SAN_HWA("coGrade", " 일산화탄소 지수"),
    GRADE_OH_ZONE("o3Grade", "오존 지수"),
    GRADE_E_SAN_HWA("no2Grade", "이산화질소 지수"),
    GRADE_PM10_24HOUR("pm10Grade", "미세먼지 PM10 24시간 등급"),
    GRADE_PM25_24HOUR("pm25Grade", "미세먼지 PM2.5 24시간 등급"),
    GRADE_PM10_01HOUR("pm10Grade1h", "미세먼지 PM10 1시간 등급"),
    GRADE_PM25_01HOUR("pm25Grade1h", "미세먼지 PM2.5 1시간 등급"),
/*    FLAG_AH_WHANG_SAN("so2Flag", "아황산가스 플래그"),
    FLAG_IL_SAN_HWA("coFlag","일산화탄소 플래그"),
    FLAG_OH_ZONE("o3Flag","오존 플래그"),
    FLAG_E_SAN_HWA("no2Flag","이산화질소 플래그"),
    FLAG_PM10("pm10Flag", "미세먼지 PM10 플래그"),
    FLAG_PM25("pm25Flag", "마세먼지 PM2.5 플래그"),*/
}

/**
 *
 */
enum class WindDirection(val DEGREE: Float, val text: String) {
    N(0f, "북"),
    NE(45f, "북동"),
    E(90f, "동"),
    SE(135f, "남동"),
    S(180f, "남"),
    SW(225f, "남서"),
    W(270f, "서"),
    NW(315f, "북서"),
}

/**
 *
 */
enum class DustIndicator(val container: Int, val color:Int, val pointer: Int, val icon: Int, val label: String) {
    VERY_GOOD(
        R.drawable.home_dust_detail_panel_indicator_container_1,
        R.color.home_dust_detail_panel_divider_color_1,
        R.drawable.home_dust_detail_panel_indicator_dot_1,
        R.drawable.ic_dust_very_good,
        "매우좋음"
    ),
    GOOD(
        R.drawable.home_dust_detail_panel_indicator_container_2,
        R.color.home_dust_detail_panel_divider_color_2,
        R.drawable.home_dust_detail_panel_indicator_dot_2,
        R.drawable.ic_dust_good,
        "좋음"
    ),
    NORMAL(
        R.drawable.home_dust_detail_panel_indicator_container_3,
        R.color.home_dust_detail_panel_divider_color_3,
        R.drawable.home_dust_detail_panel_indicator_dot_3,
        R.drawable.ic_dust_normal,
        "보통"
    ),
    BAD(
        R.drawable.home_dust_detail_panel_indicator_container_4,
        R.color.home_dust_detail_panel_divider_color_4,
        R.drawable.home_dust_detail_panel_indicator_dot_4,
        R.drawable.ic_dust_bad,
        "나쁨"
    ),
    VERY_BAD(
        R.drawable.home_dust_detail_panel_indicator_container_5,
        R.color.home_dust_detail_panel_divider_color_5,
        R.drawable.home_dust_detail_panel_indicator_dot_5,
        R.drawable.ic_dust_very_bad,
        "매우나쁨"
    ),
}