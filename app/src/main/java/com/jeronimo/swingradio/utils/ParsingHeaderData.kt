package com.jeronimo.swingradio.utils

import android.text.TextUtils
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by gleb on 24.12.15.
 */
class ParsingHeaderData {

    lateinit var streamUrl: URL
    private var metadata: Map<String, String>? = null
    private lateinit var trackData: TrackData

    private var con: URLConnection? = null
    private var stream: InputStream? = null
    private var headerList: List<String>? = null

    inner class TrackData {
        var artist = "Unresolved"
        var title = "Unresolved"
    }

    fun getTrackDetails(streamUrl: URL): TrackData {
        trackData = TrackData()
        this.streamUrl = streamUrl
        var strTitle = ""
        var strArtist = ""
        try {
            metadata = executeToFetchData()
            if (metadata != null) {
                var streamHeading = ""
                val data = metadata
                if (data != null) {

                    if (data.containsKey("StreamTitle")) {
                        strArtist = data.get("StreamTitle")!!
                        streamHeading = strArtist
                    }
                }
                if (!TextUtils.isEmpty(strArtist) && strArtist.contains("-")) {
                    strArtist = strArtist.substring(0, strArtist.indexOf("-"))
                    trackData!!.artist = strArtist.trim { it <= ' ' }
                }
                if (!TextUtils.isEmpty(streamHeading)) {
                    if (streamHeading.contains("-")) {
                        strTitle = streamHeading.substring(streamHeading
                                .indexOf("-") + 1)
                        trackData!!.title = strTitle.trim { it <= ' ' }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return trackData
    }

    @Throws(IOException::class)
    private fun executeToFetchData(): Map<String, String>? {
        try {
            con = streamUrl.openConnection()

            con!!.setRequestProperty("Icy-MetaData", "1")
            // con.setRequestProperty("Connection", "close");
            // con.setRequestProperty("Accept", null);
            con!!.connect()

            var metaDataOffset = 0
            val headers = con!!.headerFields
            stream = con!!.getInputStream()

            if (headers.containsKey("icy-metaint")) {
                headerList = headers["icy-metaint"]
                if (headerList != null) {
                    if (headerList!!.size > 0) {
                        metaDataOffset = Integer.parseInt(headers["icy-metaint"]!!.get(0))
                    } else
                        return null
                } else
                    return null

            } else {
                return null

            }

            // In case no data was sent
            if (metaDataOffset == 0) {
                return null
            }

            // Read metadata
            var b: Int = 0
            var count = 0
            var metaDataLength = 4080 // 4080 is the max length
            var inData: Boolean
            val metaData = StringBuilder()
            while ({b = stream?.read() ?: -1; b}() != -1) {
                count++
                if (count == metaDataOffset + 1) {
                    metaDataLength = b * 16
                }
                if (count > metaDataOffset + 1 && count < metaDataOffset + metaDataLength) {
                    inData = true
                } else {
                    inData = false
                }
                if (inData) {
                    if (b != 0) {
                        metaData.append(b.toChar())
                    }
                }
                if (count > metaDataOffset + metaDataLength) {
                    break
                }

            }
            metadata = ParsingHeaderData.parsingMetadata(metaData.toString())
            stream!!.close()
        } catch (e: Exception) {
            if (e != null && e == null)
                Log.e("Error", e.message)
        } finally {
            if (stream != null)
                stream!!.close()
        }
        return metadata

    }


    companion object {

        fun parsingMetadata(metaString: String): Map<String, String> {
            val metadata = HashMap<String, String>()
            val metaParts = metaString.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val p = Pattern.compile("^([a-zA-Z]+)=\\'([^\\']*)\\'$")
            var m: Matcher
            for (i in metaParts.indices) {
                m = p.matcher(metaParts[i])
                if (m.find()) {
                    metadata.put(m.group(1) as String, m.group(2) as String)
                }
            }

            return metadata
        }
    }
}