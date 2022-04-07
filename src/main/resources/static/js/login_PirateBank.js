'use strict'
import {getUrl} from "./modules/routing.js";

import {authenticate} from "./modules/authModule.js";
import {setRegexCheckInputfields,setValidateFieldsEventListener,setValidationIconFalse,setValidationIconTrue} from "./modules/formHelper.js";

let allFieldcorrect = false;

const btn_inloggen = document.querySelector(`#btn_inloggen`)
const username_field = document.querySelector(`#inloggen_email`)
const password_field = document.querySelector(`#inloggen_password`)

document.querySelector(`.modal_close`).addEventListener(`click`,e=>hideModal())


    // validation of input field
    const  validateField = (element) => {
        if (!element.validity.valid) {
            allFieldcorrect = false;
            setValidationIconFalse(element)
            displayErrorMessage(element)
        }
        if(element.validity.valid) {
            removeErrorMessage(element)
            setValidationIconTrue(element)
        }
        element.setCustomValidity("")
    }

    // displays error message in row parent
    const displayErrorMessage = (element) => {
        // finds row_parent of element-> then queries if row parent has error message
        let row_parent_node = element.closest('.row_parent')
        let error_message =  row_parent_node.querySelector(".error_message")
        // check if there is an error message
        if (error_message !== null) {
            // if an error message already exists, replace with new error message
            error_message.textContent = getErrorMessage(element)
        } else {
            let li = document.createElement('li')
            li.className="error_message";
            li.textContent = getErrorMessage(element)
            li.style.marginLeft="15px";
            li.style.marginTop="-5px";
            li.style.fontSize = "12px";
            row_parent_node.append(li)
        }
    }

    // remove error message
    const removeErrorMessage = (element) => {
        let row_parent_node = element.closest('.row_parent')
        let error_message =  row_parent_node.querySelector(".error_message")
        if (error_message !== null) {
            // if an error message already exists, replace with new error message
            error_message.remove()
        }
    }

const getErrorMessage = function (element) {
    // Don't validate submits, buttons, file and reset inputs, and disabled fields
    if (element.disabled || element.type === 'file' || element.type === 'reset' || element.type === 'submit' || element.type === 'button') return;
    // Get validity
    let validity = element.validity;

    // If valid, return null
    if (validity.valid) return;

    if (validity.customError) return element.validationMessage

    // If element is required and empty
    if (validity.valueMissing) return 'Dit veld is verplicht.';

    // If not the right type
    if (validity.typeMismatch) {

        // Email
        if (element.type === 'email') return 'Voer een juist emailadres in.';

    }

    // If too short
    if (validity.tooShort)  {

        if (element.type === 'password') return `Het wachtwoord moet tenminste ${element.minLength} karakters  bevatten.`;

        return 'Het ingevoerde veld moet tenminste ' + element.getAttribute('minLength') + ' karakters  bevatten.';
    }

    // If too long
    if (validity.tooLong) return 'Het ingevoerde veld mag maar ' + element.getAttribute('maxLength') + ' karakters bevatten. U gebruikt op dit moment ' + element.value.length + ' karakters .';

     // If pattern doesn't match
    if (validity.patternMismatch) {

        // If pattern info is included, return custom error
        if (element.hasAttribute('title')) return element.getAttribute('title');

        if (element.type == 'email') return 'Het ingevoerde emailadres is onjuist.'

        // Otherwise, generic error
        return 'Het ingevoerde veld voldoet niet aan de juiste format.';
    }

    return 'Het veld wat u heeft ingevoerd is incorrect, probeer opnieuw.';

};

const doEmptyFieldCheck = () => {
    validateField(username_field)
    validateField(password_field)
}

btn_inloggen.addEventListener("click",doEmptyFieldCheck)
setValidateFieldsEventListener()
setRegexCheckInputfields()

document.querySelector('#btn_inloggen').addEventListener('click', (e)=> {
    authenticate(makeLoginData(),showError)

})

const showError = (json) => {
    let error = json.Error;
    if (error.includes("gebruikersnaam")){
        username_field.setCustomValidity(error);
        validateField(username_field);
    }
}

const makeLoginData = () => {
    let  username_value = document.querySelector(`#inloggen_email`).value;
    let  password_value = document.querySelector(`#inloggen_password`).value;

    let login_data = {username: username_value,
        password: password_value};
    return login_data

}
