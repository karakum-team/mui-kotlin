import BasicBreadcrumbs from './Breadcrumbs'
import BasicTextFields from './TextFields'
import { Chip } from '@mui/material'

function App () {
  return <>
    <Chip label={"Breadcrumbs"}/>
    <BasicBreadcrumbs/>

    <br/>

    <Chip label={"Text Fields"}/>
    <BasicTextFields/>
  </>
}

export default App
