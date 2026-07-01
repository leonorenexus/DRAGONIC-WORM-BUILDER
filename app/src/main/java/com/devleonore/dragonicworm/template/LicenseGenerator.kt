package com.devleonore.dragonicworm.template

import com.devleonore.dragonicworm.model.License
import java.util.Calendar

object LicenseGenerator {

    fun generate(license: String, author: String): String {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        return when (license.uppercase()) {
            "MIT" -> mit(author, year)
            "APACHE2" -> apache2(author, year)
            "GPL3" -> gpl3(author, year)
            else -> custom(author, year)
        }
    }

    private fun mit(author: String, year: Int) = """
        |MIT License
        |
        |Copyright (c) $year $author
        |
        |Permission is hereby granted, free of charge, to any person obtaining a copy
        |of this software and associated documentation files (the "Software"), to deal
        |in the Software without restriction, including without limitation the rights
        |to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        |copies of the Software, and to permit persons to whom the Software is
        |furnished to do so, subject to the following conditions:
        |
        |The above copyright notice and this permission notice shall be included in all
        |copies or substantial portions of the Software.
        |
        |THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        |IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        |FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
    """.trimMargin()

    private fun apache2(author: String, year: Int) = """
        |Apache License 2.0
        |
        |Copyright $year $author
        |
        |Licensed under the Apache License, Version 2.0 (the "License"); you may not
        |use this file except in compliance with the License.
    """.trimMargin()

    private fun gpl3(author: String, year: Int) = """
        |GNU GENERAL PUBLIC LICENSE
        |Version 3, 29 June 2007
        |
        |Copyright (C) $year $author
        |
        |This program is free software: you can redistribute it and/or modify it
        |under the terms of the GNU General Public License as published by the Free
        |Software Foundation, either version 3 of the License, or (at your option)
        |any later version.
    """.trimMargin()

    private fun custom(author: String, year: Int) = """
        |Custom License
        |
        |Copyright (c) $year $author
        |
        |All rights reserved. Unauthorized copying, modification, distribution, or
        |use is strictly prohibited.
    """.trimMargin()
}
