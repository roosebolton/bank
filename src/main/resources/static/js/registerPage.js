'use strict'
import {getUrl} from "./modules/routing.js";
import {setRegexCheckInputfields,setValidateFieldsEventListener,setIconsInputfield,checkforIcons,
    setValidationIconFalse,setValidationIconTrue,checkRegex} from "./module.js";

let allFieldcorrect = false;

/* html container/ buttons for hiding and displaying different parts of the page   */
const register_user_container = document.querySelector(`#register_user`)
const register_clientinfo_container = document.querySelector(`#register_clientinfo`)
const btn_nextPage = document.querySelector(`#btn_nextPage`)
const btn_previous = document.querySelector(`.btn_previous`)
const btn_registrationsuccess = document.querySelector(`#btn_registrationsuccess`)

/* modal  */
const modal = document.querySelector(`.modal`)
const modal_success = document.querySelector(`.modal_success`)
const modal_failure = document.querySelector(`.modal_failure`)
document.querySelector(`.modal_close`).addEventListener(`click`,e=>hideModal())

/* register input fields */
const username_field = document.querySelector(`#register_email`)
const password_field = document.querySelector(`#register_password`)
const firstname_field = document.querySelector(`#register_firstname`)
const infix_field = document.querySelector(`#register_infix`)
const lastname_field = document.querySelector(`#register_lastname`)
const dateOfBirth_field = document.querySelector(`#register_birthday`)
const BSN_number_field = document.querySelector(`#register_BSN_number`)
const postcode_field = document.querySelector(`#register_postcode`)
const streetnumber_field = document.querySelector(`#register_streetnumber`)
const register_streetnumberAddition_field = document.querySelector(`#register_streetnumberAddition`)
const streetname_field = document.querySelector(`#register_streetname`)
const city_field = document.querySelector(`#register_city`)
const iban_field = document.querySelector(`#register_IBAN`)


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
    if (element.disabled || element.type === 'submit' || element.type === 'button') return;
    // Get validity
    let validity = element.validity;

    // If valid, return null
    if (validity.valid) return;

    if (validity.customError) return element.validationMessage

    // If element is required and empty
    if (validity.valueMissing) return 'Dit veld is verplicht.';

    // If not the right type
    if (validity.typeMismatch && element.type === 'email') {
        return 'Voer een juist emailadres in.';
    }

    // If too short
    if (validity.tooShort)  {

        if (element.type === 'email') return `De ingevoerde email moet tenminste ${element.minLength} karakters lang zijn, uw ingevoerde email is ${element.value.length} karakters lang.`;

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

        if (element.type == 'password') return 'Het ingevoerde wachtwoord moet de juiste tekens bevatten.'
        if (element.type == 'postcode') return 'De ingevoerde postcode is onjuist.'

        // Otherwise, generic error
        return 'Het ingevoerde veld voldoet niet aan de juiste format.';
    }

    return 'Het veld wat u heeft ingevoerd is incorrect, probeer opnieuw.';

};

// checkAddress
const checkAddress = () => {
    let postcode = document.getElementById('register_postcode').value
    let streetnumber = document.getElementById('register_streetnumber').value
    if (postcode != "" && streetnumber != "") {
        let formData = `postcode=` + postcode + `&number=` + streetnumber

        fetch("https://postcode.tech/api/v1/postcode?" + formData, {
            headers: {
                'Authorization': 'Bearer d565125a-aedc-48f7-bc38-3805da112237',
            },
        })
            .then(response => response.json())
            .then(json => processAddress(json))
            .catch((error) => { console.error('netwerkfout:', error)});
    }
}

const  processAddress = (data) =>{
    let addressPart = data;
    if (addressPart.city != null || addressPart.street != null) {
        city_field.value = addressPart.city;
        streetname_field.value = addressPart.street;

        // removes any error messages
        city_field.setCustomValidity("");
        streetname_field.setCustomValidity("");

        // normal validation styling
        validateField(city_field);
        validateField(streetname_field);

        setSuccessStylingFetch(city_field);
        setSuccessStylingFetch(streetname_field);
    } else {
        city_field.setCustomValidity("Geen juist adres ingevoerd.");
        streetname_field.setCustomValidity("Geen juist adres ingevoerd.");
        validateField(city_field);
        validateField(streetname_field);
        city_field.value = "";
        streetname_field.value = "";
    }
}
    // sets extra green background for succes styling
    const setSuccessStylingFetch = (element) => {
        element.parentNode.style.backgroundColor = "#F0FFF4"
    }

    const checkFinalInputFields = () => {
        showAllFields();
        allFieldcorrect = true;
        // validate fields on more time
        document.querySelectorAll('input') .forEach(element=>validateField(element))

        if (allFieldcorrect) {
            hideInnerContainerModal();
            showModal();
            postFetchRegister(makeRegisterForm());
        }
    }

    const makeRegisterForm =  () =>  {
        let username = username_field.value;
        let password = password_field.value;
        let firstname = firstname_field.value;
        let infix = infix_field.value;
        let lastname = lastname_field.value;
        let dateOfBirth = dateOfBirth_field.value;
        let BSN_number = BSN_number_field.value;
        let postcode = postcode_field.value;
        let streetnumber = streetnumber_field.value;
        let streetnumberAddition = register_streetnumberAddition_field.value;
        let streetname = streetname_field.value;
        let city = city_field.value;
        let iban = iban_field.value;

        let formdata = { emailadres: username, wachtwoord: password,
            voornaam: firstname, achternaam: lastname, tussenvoegsel: infix, geboortedatum: dateOfBirth,
            bsnnummer: BSN_number, postcode: postcode, huisnummer: streetnumber, huisnummertoevoeging: streetnumberAddition, straat: streetname, woonplaats: city,
            ibannummer: iban
        };

        return formdata
    }

    const postFetchRegister = (formdata) => {
        fetch (getUrl("register"), {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }, body: JSON.stringify(formdata)
        }).then(response => {
                if (response.status === 201) {
                    registrationSuccess()
                } else  {
                    response.text().then(text => registrationFailed(text))
                }
            })
            .catch((error) => registrationFailed());
    }

    const registrationSuccess = () => {
            modal_success.style.display = "revert";
    }

    const registrationFailed = (text) => {
        if (text.includes("e-mailadres")){
            username_field.setCustomValidity(text);
            validateField(username_field);
        }

        modal_failure.style.display = "revert";
    }


    // Hiding & showing of container elements on the webpage
    const showModal = () => {
        modal.style.display = "block"
        // modal.style.visibility = "visible"
        // modal.style.opacity = "1"
    }

    const hideModal = () => {
        // modal.style.visibility = "invisible"
        // modal.style.opacity = "0"
        modal.style.display = "none"
    }

    const hideInnerContainerModal = () => {
        modal_success.style.display = "none"
        modal_failure.style.display = "none"
    }

    //go back to first page of form
    const goToRegisterUsername = () => {
        register_user_container.style.display = 'block';
        btn_previous.style.display = `none`;
        register_clientinfo_container.style.display = 'none';
    }

    // go to second page of the form
    const goToRegisterClientInfo = () => {
    validateField(username_field)
    validateField(password_field)
      if(username_field.validity.valid && password_field.validity.valid) {
        register_user_container.style.display = 'none';
        btn_previous.style.display = `block`;
        register_clientinfo_container.style.display = 'block';
        }
    }

    // shows all form fields
    const showAllFields = () => {
           register_user_container.style.display = 'block';
           register_clientinfo_container.style.display = 'block';
           btn_previous.style.display = `none`;
           btn_nextPage.style.display = `none`;
       }

    const goToLogin = () => {
    window.location.assign(getUrl("loginPage"));
    }

/* Setting eventlisteners  */
btn_nextPage.addEventListener("click",goToRegisterClientInfo)
btn_previous.addEventListener("click",goToRegisterUsername)
btn_registrationsuccess.addEventListener("click",goToLogin)
document.querySelector(`#btn_sendForm`).addEventListener("click",checkFinalInputFields)
setValidateFieldsEventListener()
setRegexCheckInputfields()
postcode_field.addEventListener(`focusout`,checkAddress)
streetnumber_field.addEventListener(`focusout`,checkAddress)


