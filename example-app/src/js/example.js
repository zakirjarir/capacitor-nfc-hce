import { NfcHce } from 'capacitor-nfc-hce';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    NfcHce.echo({ value: inputValue })
}
