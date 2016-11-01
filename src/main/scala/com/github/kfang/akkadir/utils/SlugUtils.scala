package com.github.kfang.akkadir.utils

import java.text.Normalizer

object SlugUtils {
  def slugify(str: String) = {
    Normalizer
      .normalize(str, Normalizer.Form.NFD)
      .toLowerCase
      .replaceAll(" ", "-")
      .replaceAll("[^a-z-]", "")
  }
}
