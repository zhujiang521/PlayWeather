package model

/**
 *  200	请求成功
204	请求成功，但你查询的地区暂时没有你需要的数据。
400	请求错误，可能包含错误的请求参数或缺少必选的请求参数。
401	认证失败，可能使用了错误的KEY、数字签名错误、KEY的类型错误（如使用SDK的KEY去访问Web API）。
402	超过访问次数或余额不足以支持继续访问服务，你可以充值、升级访问量或等待访问量重置。
403	无访问权限，可能是绑定的PackageName、BundleID、域名IP地址不一致，或者是需要额外付费的数据。
404	查询的数据或地区不存在。
429	超过限定的QPM（每分钟访问次数），请参考QPM说明
500	无响应或超时，接口服务异常请联系我们
 */

const val SUCCESSFUL = 200

/**
 * 通过错误码获取错误信息
 *
 * @param code 错误码
 * @return 错误信息
 */
fun getErrorText(code: Int): String = when (code) {
    204 -> "请求成功，但你查询的地区暂时没有你需要的数据。"
    400 -> "请求错误，可能包含错误的请求参数或缺少必选的请求参数。"
    401 -> "认证失败，可能使用了错误的KEY、数字签名错误、KEY的类型错误"
    402 -> "超过访问次数或余额不足以支持继续访问服务"
    403 -> "无访问权限，可能是绑定的PackageName、BundleID、域名IP地址不一致"
    404 -> "查询的数据或地区不存在。"
    429 -> "超过限定的QPM（每分钟访问次数）"
    else -> "无响应或超时，接口服务异常"
}