package com.bavostepbros.leap.domain.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;


/** 
 * @return String
 */

/** 
 * @return String
 */

/** 
 * @return String
 */

/** 
 * @return String
 */

/** 
 * @return String
 */

/** 
 * @return String
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

/** 
 * @return boolean
 */

/** 
 * @return boolean
 */

/** 
 * @return int
 */
@EqualsAndHashCode
public class Email {
    @javax.validation.constraints.Email
    @NotNull
    @Size(min = 1, message = "Please, set an email address to send the message to it")
    private String to;
    private String recipientName;
    private String subject;
    private String text;
    private String senderName;
    private String templateEngine;
}
