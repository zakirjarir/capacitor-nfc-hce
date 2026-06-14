# Capacitor NFC HCE Plugin 🚀

A powerful and fully dynamic Capacitor plugin for NFC Host Card Emulation (HCE) on Android. 
This plugin transforms your mobile device into a Smart Card Emulator (Metro cards, Access cards, Payment systems) with real-time APDU command handling directly from your JavaScript/TypeScript layer (Ionic/Vue/Capacitor).

## 🌟 Features
- **Fully Dynamic APDU Handling**: Process APDU commands dynamically in your web layer.
- **Hybrid Response Caching**: Extremely fast (<1ms) synchronous native responses using precomputed JS response caches to prevent NFC reader timeouts.
- **Asynchronous Fallback**: Uncached commands are forwarded to JS where you can compute the response dynamically (e.g., cryptographic signatures).
- **Run-time Service Management**: Enable or disable the HCE service programmatically.

## 📦 Installation

```bash
npm install capacitor-nfc-hce
npx cap sync
```

## ⚙️ Android Configuration

You must register the Application ID (AID) that your virtual card will respond to.
By default, the plugin uses `F0010203040506`. To modify this:

1. Open `android/src/main/res/xml/apduservice.xml` in your project.
2. Change the `aid-filter` to match your target system's AID:

```xml
<host-apdu-service xmlns:android="http://schemas.android.com/apk/res/android"
    android:description="@string/service_name"
    android:requireDeviceUnlock="false">
    <aid-group android:category="other" android:description="@string/aid_description">
        <!-- Change to your target AID -->
        <aid-filter android:name="F0010203040506" />
    </aid-group>
</host-apdu-service>
```

## 🚀 Usage Guide

### 1. Hybrid Caching (Recommended for Performance)
NFC Readers expect a response within ~50ms. If the Javascript bridge is too slow, the reader will timeout. To fix this, cache your predictable responses (like the `SELECT AID` command).

```typescript
import { NfcHce } from 'capacitor-nfc-hce';

// Pre-compute static responses for ultra-fast native replies
await NfcHce.setResponseCache({
  cache: {
    // SELECT AID Command -> Success (9000)
    "00A4040007F001020304050600": "9000",
    // Generic Read Command -> Mock Data
    "00B0000002": "010203049000"
  }
});
```

### 2. Start HCE & Listen to Commands
```typescript
// Start the service
await NfcHce.startHce();

// Listen to incoming APDU Commands
NfcHce.addListener('onApduCommand', async (event) => {
  console.log("NFC Reader Sent:", event.command);

  // If the command is NOT in the cache, you MUST provide a response manually
  if (event.command === '00112233') {
     const dynamicResponse = await calculateSecureResponse(event.command);
     await NfcHce.sendResponse({ response: dynamicResponse });
  }
});

// To stop the emulation:
// await NfcHce.stopHce();
```

## API

<docgen-index>

* [`echo(...)`](#echo)
* [`startHce()`](#starthce)
* [`stopHce()`](#stophce)
* [`sendResponse(...)`](#sendresponse)
* [`setResponseCache(...)`](#setresponsecache)
* [`clearResponseCache()`](#clearresponsecache)
* [`addListener('onApduCommand', ...)`](#addlisteneronapducommand-)
* [`addListener('onHceDeactivated', ...)`](#addlisteneronhcedeactivated-)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### echo(...)

```typescript
echo(options: { value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### startHce()

```typescript
startHce() => Promise<void>
```

--------------------


### stopHce()

```typescript
stopHce() => Promise<void>
```

--------------------


### sendResponse(...)

```typescript
sendResponse(options: { response: string; }) => Promise<void>
```

| Param         | Type                               |
| ------------- | ---------------------------------- |
| **`options`** | <code>{ response: string; }</code> |

--------------------


### setResponseCache(...)

```typescript
setResponseCache(options: { cache: Record<string, string>; }) => Promise<void>
```

| Param         | Type                                                                        |
| ------------- | --------------------------------------------------------------------------- |
| **`options`** | <code>{ cache: <a href="#record">Record</a>&lt;string, string&gt;; }</code> |

--------------------


### clearResponseCache()

```typescript
clearResponseCache() => Promise<void>
```

--------------------


### addListener('onApduCommand', ...)

```typescript
addListener(eventName: 'onApduCommand', listenerFunc: (data: { command: string; }) => void) => Promise<PluginListenerHandle> & PluginListenerHandle
```

| Param              | Type                                                 |
| ------------------ | ---------------------------------------------------- |
| **`eventName`**    | <code>'onApduCommand'</code>                         |
| **`listenerFunc`** | <code>(data: { command: string; }) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt; & <a href="#pluginlistenerhandle">PluginListenerHandle</a></code>

--------------------


### addListener('onHceDeactivated', ...)

```typescript
addListener(eventName: 'onHceDeactivated', listenerFunc: (data: { reason: number; }) => void) => Promise<PluginListenerHandle> & PluginListenerHandle
```

| Param              | Type                                                |
| ------------------ | --------------------------------------------------- |
| **`eventName`**    | <code>'onHceDeactivated'</code>                     |
| **`listenerFunc`** | <code>(data: { reason: number; }) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt; & <a href="#pluginlistenerhandle">PluginListenerHandle</a></code>

--------------------


### Interfaces


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


### Type Aliases


#### Record

Construct a type with a set of properties K of type T

<code>{ [P in K]: T; }</code>

</docgen-api>
