'use strict'
import {getUrl} from "./modules/routing.js";

import {authenticate} from "./modules/authModule.js";
let allFieldcorrect = false;

const btn_inloggen = document.querySelector(`#btn_inloggen`)
const username_field = document.querySelector(`#inloggen_email`)
const password_field = document.querySelector(`#inloggen_password`)

document.querySelector(`.modal_close`).addEventListener(`click`,e=>hideModal())


    // adds eventlistener which checks input on regex
    const setRegexCheckInputfields = () => {
        document.querySelectorAll('input')
            .forEach(e => e.addEventListener(`input`, e => checkRegex(e.target)))
    }

    // adds eventlistener onfocus out ( after leaving input field -> execute validatefield on element
    const setValidateFieldsEventListener= () => {
    document.querySelectorAll('input')
        .forEach(e => e.addEventListener('focusout', e => validateField(e.target)))
    }

    //setting icons for input
    const setIconsInputfield = (element, correctinput) => {
        checkforIcons(element);
        if (correctinput) {
            setCheckIcon(element)
        }
    }
    // checks for current icons in element and removes them
    const checkforIcons = (element) => {
        let input_element = element.parentNode.children;
        for (let i = 0; i < input_element.length; i++) {
            if (input_element[i].id === "check_icon" || input_element[i].id === "error_icon"  ) {
                input_element[i].remove();
            }
        }
    }

    // setting validity icons on element
    const setCheckIcon = (element) => {
        if (element.value) {
            let icon = document.createElement('i');
            icon.className = "fa-solid fa-circle-check";
            icon.id = "check_icon";
            icon.style.color = "green";
            element.parentNode.appendChild(icon);
            element.parentNode.style.display = "flex";
        }
    }
    // sets error icon
    const setErrorIcon = (element) => {
        let icon = document.createElement('i');
        icon.className = "fa-solid fa-circle-xmark";
        icon.id = "error_icon";
        icon.style.color = "red";
        icon.addEventListener("click",() => element.value="");
        element.parentNode.appendChild(icon);
        element.parentNode.style.display = "flex !important";

    }
    // setvalidation false icons & styling
    const setValidationIconFalse = (element) => {
        element.className = "invalid"
        element.parentNode.style.backgroundColor = "var(--error-red-fill)"
        element.parentNode.style.borderColor = "var(--error-red-border)"
        element.style.Color = "var(--error-red-border)"
        checkforIcons(element)
        setErrorIcon(element)
    }
    // setValidation false icons & styling
     const setValidationIconTrue = (element) => {
        element.className = "valid"
        element.parentNode.style.backgroundColor = "#FFFFFF"
        element.parentNode.style.borderColor = "#9AE6B4"
        checkforIcons(element)
        setCheckIcon(element)
    }

    // check regex of input (set in html) to input
     const checkRegex = (element) => {
        let regex = new RegExp(element.pattern);
        if (regex.test(
            element.value.trim())) {
            setIconsInputfield(element, true)
            return true;
        }
    }
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
