/*
 * QAuxiliary - An Xposed module for QQ/TIM
 * Copyright (C) 2019-2022 qwq233@qwq2333.top
 * https://github.com/cinit/QAuxiliary
 *
 * This software is non-free but opensource software: you can redistribute it
 * and/or modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either
 * version 3 of the License, or any later version and our eula as published
 * by QAuxiliary contributors.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and eula along with this software.  If not, see
 * <https://www.gnu.org/licenses/>
 * <https://github.com/cinit/QAuxiliary/blob/master/LICENSE.md>.
 */

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project

fun Project.configureAppSigningConfigsForRelease() = withAndroidApplication {
    val envKeystorePath = System.getenv("KEYSTORE_PATH")
    val useEnvKeystore = envKeystorePath != null && file(envKeystorePath).exists() && file(envKeystorePath).length() > 0L
    
    val tempKeystore = file("temp.jks")
    val useTempKeystore = !useEnvKeystore && tempKeystore.exists()

    if (!useEnvKeystore && !useTempKeystore) return@withAndroidApplication

    configure<ApplicationExtension>("android") {
        signingConfigs {
            create("release") {
                if (useEnvKeystore) {
                    storeFile = file(envKeystorePath!!)
                    storePassword = System.getenv("KEYSTORE_PASSWORD")
                    keyAlias = System.getenv("KEY_ALIAS")
                    keyPassword = System.getenv("KEY_PASSWORD")
                } else {
                    storeFile = tempKeystore
                    storePassword = "password"
                    keyAlias = "qauxv"
                    keyPassword = "password"
                }
                enableV2Signing = true
            }
        }
        buildTypes {
            release {
                signingConfig = signingConfigs.findByName("release")
            }
        }
    }
}
