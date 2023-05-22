import React from 'react';
import { MDBCol, MDBContainer, MDBRow, MDBCard, MDBCardText, MDBCardBody, MDBCardImage, MDBTypography, MDBIcon } from 'mdb-react-ui-kit';
import './Runner.css'
import { useEffect } from 'react';
export const Runner =()=> {
    if(localStorage.getItem("role")!=="runner"){
        window.location.href="/SignIn"
    }
    //get the user info from local storage
    var userName = localStorage.getItem("userName");
    var password = localStorage.getItem("password");
    var fristName = localStorage.getItem("name");
    var deliveryFees = localStorage.getItem("deliveryFees");
    var numberOfTrips = localStorage.getItem("numberOfTrips");
    var status = localStorage.getItem("status");
    var id = localStorage.getItem("id");
    var role = localStorage.getItem("role");
    
    useEffect(() => {
      fetch(`http://localhost:8080/Al-Akeel-Back-1.0-SNAPSHOT/api/runner/trips/${localStorage.getItem("id")}`,{
          method:"GET",
          headers:{
              "Content-Type":"application/json"
          }
      }).then((response)=>{
          response.json().then((result)=>{
              localStorage.setItem("numberOfTrips",result)
          })
      }).catch((err)=>{
          console.log(err)
      }
      )
  }, [])

  return (
    <section className="vh-100" style={{ backgroundColor: '#f4f5f7' }}>
      <MDBContainer className="py-5 h-100">
        <MDBRow className="justify-content-center align-items-center h-100">
          <MDBCol lg="6" className="mb-4 mb-lg-0">
            <MDBCard className="mb-3" style={{ borderRadius: '.5rem' }}>
              <MDBRow className="g-0">
                <MDBCol md="4" className="gradient-custom text-center text-white"
                  style={{ borderTopLeftRadius: '.5rem', borderBottomLeftRadius: '.5rem' }}>
                  <MDBCardImage src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava1-bg.webp"
                    alt="Avatar" className="my-5" style={{ width: '80px' }} fluid />
                  <MDBTypography tag="h5">{userName}</MDBTypography>
                  <MDBCardText>{role}</MDBCardText>
                  <MDBIcon far icon="edit mb-5" />
                </MDBCol>
                <MDBCol md="8">
                  <MDBCardBody className="p-4">
                    <MDBTypography tag="h6">Information</MDBTypography>
                    <hr className="mt-0 mb-4" />
                    <MDBRow className="pt-1">
                      <MDBCol size="6" className="mb-3">
                        <MDBTypography tag="h6"> Name</MDBTypography>
                        <MDBCardText className="text-muted">{fristName}</MDBCardText>
                      </MDBCol>
                      <MDBCol size="6" className="mb-3">
                        <MDBTypography tag="h6"> Status</MDBTypography>
                        <MDBCardText className="text-muted">{status}</MDBCardText>
                      </MDBCol>
                    </MDBRow>

                    <MDBTypography tag="h6">Information</MDBTypography>
                    <hr className="mt-0 mb-4" />
                    <MDBRow className="pt-1">
                      <MDBCol size="6" className="mb-3">
                        <MDBTypography tag="h6">id</MDBTypography>
                        <MDBCardText className="text-muted">{id}</MDBCardText>
                      </MDBCol>
                      <MDBCol size="6" className="mb-3">
                        <MDBTypography tag="h6">Delivery Fees</MDBTypography>
                        <MDBCardText className="text-muted">{deliveryFees}</MDBCardText>
                      </MDBCol>
                      <MDBCol size="6" className="mb-3">
                        <MDBTypography tag="h6">Number Of Trips</MDBTypography>
                        <MDBCardText className="text-muted">{numberOfTrips}</MDBCardText>
                      </MDBCol>

                    </MDBRow>

                    <div className="d-flex justify-content-start">
                      <a href="#!"><MDBIcon fab icon="facebook me-3" size="lg" /></a>
                      <a href="#!"><MDBIcon fab icon="twitter me-3" size="lg" /></a>
                      <a href="#!"><MDBIcon fab icon="instagram me-3" size="lg" /></a>
                    </div>
                  </MDBCardBody>
                </MDBCol>
              </MDBRow>
            </MDBCard>
          </MDBCol>
        </MDBRow>
      </MDBContainer>
    </section>
  );
}