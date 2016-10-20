package collisions

import java.sql.Date

/**
  * Created by Rostislav on 15.10.2016.
  */

case class FormattedCollisionRecord (
  zip: String,
  col_date: Date,
  key: Int,
  col_time: String,
  borough: String,
  on_street: String,
  cross_street: String,
  off_street: String,
  location: String
)
