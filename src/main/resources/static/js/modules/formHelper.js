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
export {checkRegex, setValidationIconTrue,setValidationIconFalse,setErrorIcon,setCheckIcon,checkforIcons,setIconsInputfield,setValidateFieldsEventListener,setRegexCheckInputfields};