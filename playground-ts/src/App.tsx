import BasicBreadcrumbs from './Breadcrumbs'
import BasicTextFields from './TextFields'
import BasicTimeFields from './TimeFields'
import {Chip} from '@mui/material'

function App () {
  return <>
    <Chip label={"Breadcrumbs"}/>
    <BasicBreadcrumbs/>

    <br/>

    <Chip label={"Text Fields"}/>
    <BasicTextFields/>

    <br/>

    <Chip label={"Time Fields"}/>
    <BasicTimeFields/>
  </>
}

export default App
